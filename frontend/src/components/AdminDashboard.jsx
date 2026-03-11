import { useEffect, useMemo, useState } from "react";
import { motion } from "framer-motion";
import {
  BarChart3,
  CalendarDays,
  ChevronRight,
  CreditCard,
  Package,
  Store,
} from "lucide-react";
import { Link } from "react-router";
import { getPartiteGiorno } from "../lib/api";

const toDateInputValue = (date) => {
  const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
  return local.toISOString().slice(0, 10);
};

const normalizeTime = (isoDateTime) => String(isoDateTime || "").slice(11, 16);

const AdminDashboard = ({ viewerRole, viewerUserId }) => {
  const [selectedDay, setSelectedDay] = useState(() => toDateInputValue(new Date()));
  const [partite, setPartite] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadPartite = async () => {
      setLoading(true);
      setError("");
      try {
        const data = await getPartiteGiorno({
          giorno: selectedDay,
          viewerUserId,
          viewerRole,
        });
        setPartite(data || []);
      } catch (err) {
        setError(err.message || "Errore caricamento partite.");
      } finally {
        setLoading(false);
      }
    };
    loadPartite();
  }, [selectedDay, viewerRole, viewerUserId]);

  const summary = useMemo(() => {
    const totali = partite.length;
    const attesa = partite.filter((item) => item.stato === "IN_ATTESA_CONFERME").length;
    return { totali, attesa };
  }, [partite]);

  return (
    <div className="space-y-4 pb-24">
      <motion.div
        className="rounded-3xl border border-sky-100 bg-white p-5 shadow-xl shadow-sky-100/70"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <h2 className="text-xl font-black text-slate-900">Partite Prenotate</h2>
        <p className="mt-1 text-sm text-slate-600">
          Vista amministrativa completa di prenotazioni future e in attesa.
        </p>

        <div className="mt-4 grid gap-3 sm:grid-cols-3">
          <div className="rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2">
            <p className="text-xs font-semibold text-slate-500">Data</p>
            <input
              type="date"
              value={selectedDay}
              onChange={(event) => setSelectedDay(event.target.value)}
              className="mt-1 w-full rounded-lg border border-slate-300 px-2 py-1 text-sm"
            />
          </div>
          <div className="rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2">
            <p className="text-xs font-semibold text-slate-500">Partite Totali</p>
            <p className="mt-1 text-2xl font-black text-slate-900">{summary.totali}</p>
          </div>
          <div className="rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2">
            <p className="text-xs font-semibold text-slate-500">In Attesa</p>
            <p className="mt-1 text-2xl font-black text-red-600">{summary.attesa}</p>
          </div>
        </div>
      </motion.div>

      <motion.div
        className="rounded-3xl border border-slate-200 bg-white p-4 shadow-md"
        initial={{ opacity: 0, y: 25 }}
        animate={{ opacity: 1, y: 0 }}
      >
        {loading ? (
          <p className="text-sm text-slate-500">Caricamento prenotazioni...</p>
        ) : error ? (
          <p className="rounded-xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-700">{error}</p>
        ) : partite.length === 0 ? (
          <p className="text-sm text-slate-500">Nessuna partita trovata per questa data.</p>
        ) : (
          <div className="space-y-2">
            {partite.map((partita) => (
              <article
                key={partita.partitaId}
                className="rounded-2xl border border-slate-100 bg-slate-50 p-3"
              >
                <div className="flex items-center justify-between gap-2">
                  <p className="text-sm font-extrabold text-slate-900">
                    {normalizeTime(partita.dataOraInizio)} - {normalizeTime(partita.dataOraFine)}
                  </p>
                  <span className="rounded-full bg-red-100 px-2 py-0.5 text-[11px] font-bold text-red-700">
                    {partita.stato}
                  </span>
                </div>
                <p className="mt-1 text-xs text-slate-500">
                  {partita.campoNome || "Campo"} • {(partita.partecipanti || []).length} giocatori
                </p>
              </article>
            ))}
          </div>
        )}
      </motion.div>

      <motion.div
        className="grid gap-3 md:grid-cols-2"
        initial={{ opacity: 0, y: 22 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <Link
          to="/statistiche"
          className="rounded-3xl border border-slate-200 bg-white p-4 shadow-md transition hover:shadow-lg"
        >
          <div className="mb-3 flex items-center justify-between">
            <span className="rounded-xl bg-red-500 p-2 text-white shadow-lg shadow-red-500/40">
              <BarChart3 size={18} />
            </span>
            <ChevronRight className="text-slate-500" />
          </div>
          <h3 className="text-base font-black text-slate-900">Statistiche</h3>
          <p className="text-sm text-slate-600">Contenitori Chart.js in stile premium.</p>
        </Link>

        <Link
          to="/magazzino"
          className="rounded-3xl border border-slate-200 bg-white p-4 shadow-md transition hover:shadow-lg"
        >
          <div className="mb-3 flex items-center justify-between">
            <span className="rounded-xl bg-slate-900 p-2 text-white">
              <Store size={18} />
            </span>
            <ChevronRight className="text-slate-500" />
          </div>
          <h3 className="text-base font-black text-slate-900">Magazzino</h3>
          <p className="text-sm text-slate-600">Gestione prodotti e rifornimenti.</p>
        </Link>
      </motion.div>

      <div className="fixed bottom-20 left-4 right-4 z-20 rounded-2xl border border-slate-200 bg-white p-2 shadow-xl md:hidden">
        <div className="grid grid-cols-3 gap-1">
          <Link
            to="/cassa"
            className="flex flex-col items-center gap-1 rounded-xl py-2 text-[11px] font-semibold text-slate-700 hover:bg-slate-100"
          >
            <CreditCard size={16} />
            Cassa
          </Link>
          <Link
            to="/calendario"
            className="flex flex-col items-center gap-1 rounded-xl py-2 text-[11px] font-semibold text-slate-700 hover:bg-slate-100"
          >
            <CalendarDays size={16} />
            Prenota
          </Link>
          <Link
            to="/statistiche"
            className="flex flex-col items-center gap-1 rounded-xl py-2 text-[11px] font-semibold text-slate-700 hover:bg-slate-100"
          >
            <BarChart3 size={16} />
            Stats
          </Link>
        </div>
      </div>

      <div className="hidden rounded-3xl border border-slate-200 bg-white p-4 shadow-md md:block">
        <div className="grid grid-cols-3 gap-3 text-sm">
          <Link
            to="/cassa"
            className="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 font-semibold text-slate-700"
          >
            <CreditCard size={16} />
            Cassa
          </Link>
          <Link
            to="/calendario"
            className="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 font-semibold text-slate-700"
          >
            <CalendarDays size={16} />
            Prenota Partite
          </Link>
          <Link
            to="/magazzino"
            className="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 font-semibold text-slate-700"
          >
            <Package size={16} />
            Magazzino
          </Link>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
