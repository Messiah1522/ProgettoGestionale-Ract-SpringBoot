import { useEffect, useMemo, useState } from "react";
import { useOutletContext } from "react-router";
import { getProdotti } from "../lib/api";

const Magazzino = () => {
  const { isAdmin } = useOutletContext();

  const [prodotti, setProdotti] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [search, setSearch] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [minQty, setMinQty] = useState("");

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const data = await getProdotti();
        setProdotti(data || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  const filtered = useMemo(() => {
    return prodotti.filter((prodotto) => {
      const titolo = (prodotto.titolo || "").toLowerCase();
      const prezzo = Number(prodotto.prezzo || 0);
      const qta = Number(prodotto.quantitaMagazzino || 0);

      if (search && !titolo.includes(search.toLowerCase())) return false;
      if (minPrice !== "" && prezzo < Number(minPrice)) return false;
      if (maxPrice !== "" && prezzo > Number(maxPrice)) return false;
      if (minQty !== "" && qta < Number(minQty)) return false;
      return true;
    });
  }, [prodotti, search, minPrice, maxPrice, minQty]);

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
        <h2 className="mb-3 text-lg font-bold text-slate-800">Magazzino</h2>

        <div className="grid gap-2 md:grid-cols-4">
          <input
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            placeholder="Cerca prodotto"
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
          <input
            type="number"
            min="0"
            value={minPrice}
            onChange={(event) => setMinPrice(event.target.value)}
            placeholder="Prezzo min"
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
          <input
            type="number"
            min="0"
            value={maxPrice}
            onChange={(event) => setMaxPrice(event.target.value)}
            placeholder="Prezzo max"
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
          <input
            type="number"
            min="0"
            value={minQty}
            onChange={(event) => setMinQty(event.target.value)}
            placeholder="Qta min"
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </div>
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        {loading ? (
          <p className="text-sm text-slate-500">Caricamento magazzino...</p>
        ) : error ? (
          <p className="rounded-lg bg-red-50 px-3 py-2 text-sm font-medium text-red-700">{error}</p>
        ) : filtered.length === 0 ? (
          <p className="text-sm text-slate-500">Nessun prodotto trovato con i filtri impostati.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 text-left text-xs uppercase tracking-wide text-slate-500">
                  <th className="px-2 py-2">Prodotto</th>
                  <th className="px-2 py-2">Prezzo</th>
                  <th className="px-2 py-2">Disponibilità</th>
                  <th className="px-2 py-2">Stato</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((prodotto) => {
                  const disponibilita = Number(prodotto.quantitaMagazzino || 0);
                  return (
                    <tr key={prodotto.id} className="border-b border-slate-100">
                      <td className="px-2 py-2 font-medium text-slate-800">{prodotto.titolo}</td>
                      <td className="px-2 py-2">EUR {Number(prodotto.prezzo || 0).toFixed(2)}</td>
                      <td className="px-2 py-2">
                        <span
                          className={`font-semibold ${
                            disponibilita <= 5 ? "text-red-600" : "text-slate-700"
                          }`}
                        >
                          {disponibilita}
                        </span>
                      </td>
                      <td className="px-2 py-2">
                        <span
                          className={`rounded-full px-2 py-0.5 text-xs font-semibold ${
                            prodotto.disponibile
                              ? "bg-green-100 text-green-700"
                              : "bg-slate-100 text-slate-600"
                          }`}
                        >
                          {prodotto.disponibile ? "Disponibile" : "Non disponibile"}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </section>
  );
};

export default Magazzino;
