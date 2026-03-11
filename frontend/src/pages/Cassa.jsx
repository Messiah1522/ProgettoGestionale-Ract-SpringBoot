import { useEffect, useMemo, useState } from "react";
import { useOutletContext } from "react-router";
import {
  createTransazioneCassa,
  getGiocatoriPresenti,
  getProdotti,
} from "../lib/api";

const paymentOptions = ["CONTANTI", "CARTA", "SATISPAY", "BONIFICO", "ALTRO"];

const Cassa = () => {
  const { isAdmin, viewerRole } = useOutletContext();

  const [prodotti, setProdotti] = useState([]);
  const [giocatori, setGiocatori] = useState([]);
  const [carrello, setCarrello] = useState([]);
  const [search, setSearch] = useState("");
  const [cliente, setCliente] = useState("occasionale");
  const [clienteOccasionaleNome, setClienteOccasionaleNome] = useState("Cliente Occasionale");
  const [metodoPagamento, setMetodoPagamento] = useState("CONTANTI");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  const loadData = async () => {
    setLoading(true);
    setError("");
    try {
      const [p, g] = await Promise.all([getProdotti(), getGiocatoriPresenti()]);
      setProdotti((p || []).filter((item) => Number(item.quantitaMagazzino || 0) > 0));
      setGiocatori(g || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const prodottiFiltrati = useMemo(() => {
    if (!search) return prodotti;
    return prodotti.filter((p) =>
      String(p.titolo || "")
        .toLowerCase()
        .includes(search.toLowerCase())
    );
  }, [prodotti, search]);

  const total = useMemo(
    () =>
      carrello.reduce(
        (sum, item) => sum + Number(item.prodotto.prezzo || 0) * Number(item.quantita || 0),
        0
      ),
    [carrello]
  );

  const addToCart = (prodotto) => {
    setCarrello((prev) => {
      const existing = prev.find((row) => row.prodotto.id === prodotto.id);
      if (existing) {
        return prev.map((row) =>
          row.prodotto.id === prodotto.id
            ? {
                ...row,
                quantita: Math.min(
                  row.quantita + 1,
                  Number(prodotto.quantitaMagazzino || row.quantita + 1)
                ),
              }
            : row
        );
      }
      return [...prev, { prodotto, quantita: 1 }];
    });
  };

  const updateQty = (prodottoId, qty) => {
    setCarrello((prev) =>
      prev
        .map((row) =>
          row.prodotto.id === prodottoId
            ? {
                ...row,
                quantita: Math.max(0, Math.min(Number(qty), Number(row.prodotto.quantitaMagazzino || 0))),
              }
            : row
        )
        .filter((row) => row.quantita > 0)
    );
  };

  const handleCheckout = async () => {
    if (!carrello.length) {
      setNotice("Carrello vuoto.");
      return;
    }

    try {
      setNotice("");
      await createTransazioneCassa({
        viewerRole,
        body: {
          clienteUtenteId: cliente === "occasionale" ? null : Number(cliente),
          clienteOccasionale: cliente === "occasionale",
          nominativoClienteOccasionale:
            cliente === "occasionale" ? clienteOccasionaleNome : null,
          metodoPagamento,
          tipo: "VENDITA_MAGAZZINO",
          righe: carrello.map((row) => ({
            prodottoId: row.prodotto.id,
            quantita: row.quantita,
          })),
        },
      });

      setCarrello([]);
      setNotice("Pagamento registrato e magazzino aggiornato.");
      await loadData();
    } catch (err) {
      setNotice(`Errore checkout: ${err.message}`);
    }
  };

  if (!isAdmin) {
    return (
      <section className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm font-medium text-red-700">
        Accesso riservato ad Admin/SuperAdmin.
      </section>
    );
  }

  return (
    <section className="space-y-4">
      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <h2 className="mb-3 text-lg font-bold text-slate-800">Cassa POS</h2>

        <div className="grid gap-2 md:grid-cols-3">
          <select
            value={cliente}
            onChange={(event) => setCliente(event.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          >
            <option value="occasionale">Cliente Occasionale</option>
            {giocatori.map((g) => (
              <option key={g.id} value={g.id}>
                {g.nome} {g.cognome}
              </option>
            ))}
          </select>

          {cliente === "occasionale" && (
            <input
              value={clienteOccasionaleNome}
              onChange={(event) => setClienteOccasionaleNome(event.target.value)}
              placeholder="Nome cliente occasionale"
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
            />
          )}

          <select
            value={metodoPagamento}
            onChange={(event) => setMetodoPagamento(event.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          >
            {paymentOptions.map((method) => (
              <option key={method} value={method}>
                {method}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <div className="mb-3 flex items-center justify-between gap-2">
            <h3 className="text-sm font-bold uppercase tracking-wide text-slate-500">Prodotti</h3>
            <input
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Ricerca veloce"
              className="rounded-lg border border-slate-300 px-3 py-1.5 text-sm"
            />
          </div>

          {loading ? (
            <p className="text-sm text-slate-500">Caricamento prodotti...</p>
          ) : error ? (
            <p className="rounded-lg bg-red-50 px-3 py-2 text-sm font-medium text-red-700">{error}</p>
          ) : (
            <div className="grid grid-cols-2 gap-2">
              {prodottiFiltrati.map((prodotto) => (
                <button
                  key={prodotto.id}
                  type="button"
                  onClick={() => addToCart(prodotto)}
                  className="rounded-xl border border-blue-200 bg-blue-50 px-3 py-3 text-left hover:bg-blue-100"
                >
                  <p className="text-sm font-semibold text-slate-800">{prodotto.titolo}</p>
                  <p className="text-xs text-slate-500">EUR {Number(prodotto.prezzo || 0).toFixed(2)}</p>
                  <p className="mt-1 text-[11px] font-medium text-slate-500">
                    Disp: {prodotto.quantitaMagazzino}
                  </p>
                </button>
              ))}
            </div>
          )}
        </div>

        <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <h3 className="mb-3 text-sm font-bold uppercase tracking-wide text-slate-500">Carrello</h3>

          {!carrello.length ? (
            <p className="text-sm text-slate-500">Nessun prodotto selezionato.</p>
          ) : (
            <div className="space-y-2">
              {carrello.map((row) => (
                <div
                  key={row.prodotto.id}
                  className="flex items-center justify-between rounded-lg bg-slate-50 px-3 py-2"
                >
                  <div>
                    <p className="text-sm font-medium text-slate-800">{row.prodotto.titolo}</p>
                    <p className="text-xs text-slate-500">
                      EUR {Number(row.prodotto.prezzo || 0).toFixed(2)}
                    </p>
                  </div>

                  <input
                    type="number"
                    min="0"
                    max={row.prodotto.quantitaMagazzino}
                    value={row.quantita}
                    onChange={(event) => updateQty(row.prodotto.id, event.target.value)}
                    className="w-16 rounded-md border border-slate-300 px-2 py-1 text-right text-sm"
                  />
                </div>
              ))}
            </div>
          )}

          <div className="mt-4 rounded-xl border border-green-200 bg-green-50 px-3 py-3">
            <p className="text-xs font-semibold uppercase tracking-wide text-green-700">Totale</p>
            <p className="text-2xl font-black text-green-800">EUR {total.toFixed(2)}</p>
          </div>

          {notice && (
            <p className="mt-3 rounded-lg bg-slate-100 px-3 py-2 text-sm font-medium text-slate-700">
              {notice}
            </p>
          )}

          <button
            type="button"
            onClick={handleCheckout}
            className="mt-4 w-full rounded-xl bg-green-600 px-4 py-3 text-sm font-bold text-white hover:bg-green-700"
          >
            Paga e Registra
          </button>
        </div>
      </div>
    </section>
  );
};

export default Cassa;
