import { getStoredSession } from "./session";

const API_BASE =
  import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, "") ||
  "http://localhost:8081/api";

const buildQuery = (params = {}) => {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === null || value === "") return;
    query.append(key, String(value));
  });
  const encoded = query.toString();
  return encoded ? `?${encoded}` : "";
};

const request = async (path, options = {}) => {
  const session = getStoredSession();
  const token = session?.token;

  const res = await fetch(`${API_BASE}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
    ...options,
  });

  const isJson = res.headers.get("content-type")?.includes("application/json");
  const payload = isJson ? await res.json() : null;

  if (!res.ok) {
    const message = payload?.message || payload?.error || `HTTP ${res.status}`;
    throw new Error(message);
  }

  return payload;
};

export const getCampi = () => request("/campi");

export const login = ({ identifier, password }) =>
  request("/auth/login", {
    method: "POST",
    body: JSON.stringify({ identifier, password }),
  });

export const getSlotOccupati = (campoId, giorno) =>
  request(`/prenotazioni/slot-occupati${buildQuery({ campoId, giorno })}`);

export const getPartiteGiorno = ({ giorno, campoId, viewerUserId, viewerRole }) =>
  request(
    `/prenotazioni/giorno${buildQuery({
      giorno,
      campoId,
      viewerUserId,
      viewerRole,
    })}`
  );

export const createPrenotazione = ({ requestBody, viewerUserId, viewerRole }) =>
  request(
    `/prenotazioni${buildQuery({
      viewerUserId,
      viewerRole,
    })}`,
    {
      method: "POST",
      body: JSON.stringify(requestBody),
    }
  );

export const searchAmici = ({ query, excludeUserId, limit = 8 }) =>
  request(
    `/prenotazioni/amici${buildQuery({
      q: query,
      excludeUserId,
      limit,
    })}`
  );

export const getPagamentiPartita = ({ partitaId, viewerUserId, viewerRole }) =>
  request(
    `/partite/${partitaId}/pagamenti${buildQuery({
      viewerUserId,
      viewerRole,
    })}`
  );

export const getProdotti = () => request("/prodotti");

export const getGiocatoriPresenti = () => request("/cassa/giocatori-presenti");

export const createTransazioneCassa = ({ body, viewerRole }) =>
  request(`/cassa/transazioni${buildQuery({ viewerRole })}`, {
    method: "POST",
    body: JSON.stringify(body),
  });

export { API_BASE };
