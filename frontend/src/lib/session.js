export const STORAGE_KEY = "appia-padel-session";

const VALID_ROLES = new Set(["ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN"]);

export const getStoredSession = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw);

    const viewerRole = VALID_ROLES.has(parsed?.viewerRole) ? parsed.viewerRole : null;
    const viewerUserId = Number(parsed?.viewerUserId);
    const token = String(parsed?.token || "").trim();
    if (!viewerRole || !Number.isFinite(viewerUserId) || viewerUserId <= 0 || !token) {
      return null;
    }

    return {
      token,
      viewerRole,
      viewerUserId,
      nome: parsed?.nome || "",
      cognome: parsed?.cognome || "",
      email: parsed?.email || "",
    };
  } catch {
    return null;
  }
};

export const saveSession = (session) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
};

export const clearSession = () => {
  localStorage.removeItem(STORAGE_KEY);
};

export const buildSessionFromLogin = (loginPayload) => {
  const viewerRole = VALID_ROLES.has(loginPayload?.role) ? loginPayload.role : "ROLE_USER";
  const viewerUserId = Number(loginPayload?.userId || 0);
  if (!Number.isFinite(viewerUserId) || viewerUserId <= 0) {
    throw new Error("Risposta login non valida: userId mancante.");
  }

  return {
    token: String(loginPayload?.token || `demo-jwt-${viewerUserId}-${Date.now()}`),
    viewerRole,
    viewerUserId,
    nome: loginPayload?.nome || "",
    cognome: loginPayload?.cognome || "",
    email: loginPayload?.email || "",
  };
};
