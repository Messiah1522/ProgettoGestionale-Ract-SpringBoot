import { useEffect, useMemo, useState } from "react";
import { useOutletContext } from "react-router";
import PaymentStatusBadge from "../components/PaymentStatusBadge";
import { getPagamentiPartita, getPartiteGiorno } from "../lib/api";

const toDateInputValue = (date) => {
  const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
  return local.toISOString().slice(0, 10);
};

const normalizeTime = (isoDateTime) => String(isoDateTime || "").slice(11, 16);

const Partite = () => {
  const { viewerRole, viewerUserId } = useOutletContext();

  const [selectedDay, setSelectedDay] = useState(() => toDateInputValue(new Date()));
  const [partite, setPartite] = useState([]);
  const [pagamentiByPartita, setPagamentiByPartita] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError("");
      try {
        const partiteDelGiorno = await getPartiteGiorno({
          giorno: selectedDay,
          viewerUserId,
          viewerRole,
        });
        setPartite(partiteDelGiorno || []);

        const pagamentiEntries = await Promise.all(
          (partiteDelGiorno || []).map(async (partita) => {
            try {
              const pagamenti = await getPagamentiPartita({
                partitaId: partita.partitaId,
                viewerUserId,
                viewerRole,
              });
              return [partita.partitaId, pagamenti];
            } catch {
              return [partita.partitaId, null];
            }
          })
        );

        setPagamentiByPartita(Object.fromEntries(pagamentiEntries));
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [selectedDay, viewerRole, viewerUserId]);

  const totalPartite = useMemo(() => partite.length, [partite]);

  return (
    <section className="space-y-4">
      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <h2 className="text-lg font-bold text-slate-800">Partite e Pagamenti</h2>

          <div className="flex items-center gap-2">
            <input
              type="date"
              value={selectedDay}
              onChange={(event) => setSelectedDay(event.target.value)}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
            />
            <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600">
              {totalPartite} partite
            </span>
          </div>
        </div>

        {error && (
          <p className="mt-3 rounded-lg bg-red-50 px-3 py-2 text-sm font-medium text-red-700">
            {error}
          </p>
        )}
      </div>

      {loading ? (
        <div className="rounded-2xl border border-slate-200 bg-white p-4 text-sm text-slate-500 shadow-sm">
          Caricamento partite...
        </div>
      ) : partite.length === 0 ? (
        <div className="rounded-2xl border border-slate-200 bg-white p-4 text-sm text-slate-500 shadow-sm">
          Nessuna partita nel giorno selezionato.
        </div>
      ) : (
        <div className="space-y-3">
          {partite.map((partita) => {
            const pagamenti = pagamentiByPartita[partita.partitaId];
            return (
              <article
                key={partita.partitaId}
                className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm"
              >
                <div className="flex items-center justify-between gap-3">
                  <div>
                    <p className="text-base font-bold text-slate-800">
                      {normalizeTime(partita.dataOraInizio)} - {normalizeTime(partita.dataOraFine)}
                    </p>
                    <p className="text-sm text-slate-500">{partita.campoNome || "Campo"}</p>
                  </div>
                  {pagamenti ? (
                    <PaymentStatusBadge partita={pagamenti} />
                  ) : (
                    <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-500">
                      Pagamenti N/D
                    </span>
                  )}
                </div>

                <ul className="mt-3 grid gap-2 text-sm text-slate-700 md:grid-cols-2">
                  {(partita.partecipanti || []).map((p, index) => (
                    <li
                      key={`${partita.partitaId}-${index}`}
                      className="rounded-lg bg-slate-50 px-3 py-2"
                    >
                      <span className="font-medium">
                        {p.nome} {p.cognome}
                      </span>
                      {!p.confermato && (
                        <span className="ml-2 text-xs font-semibold text-amber-700">
                          In attesa
                        </span>
                      )}
                    </li>
                  ))}
                </ul>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
};

export default Partite;
