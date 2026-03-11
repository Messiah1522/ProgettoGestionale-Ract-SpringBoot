import { motion } from "framer-motion";
import { CalendarDays, ChevronRight, History, ShieldCheck } from "lucide-react";
import { Link } from "react-router";

const UserDashboard = () => {
  return (
    <div className="space-y-4">
      <motion.div
        className="rounded-3xl border border-sky-100 bg-white p-5 shadow-xl shadow-sky-100/70"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <p className="mb-2 inline-flex items-center gap-2 rounded-full bg-sky-100 px-3 py-1 text-xs font-bold text-sky-700">
          <ShieldCheck size={14} />
          Area Utente
        </p>
        <h2 className="text-xl font-black text-slate-900">Gioca oggi, traccia i progressi</h2>
        <p className="mt-2 text-sm text-slate-600">
          Prenota il campo in pochi tap e consulta il tuo storico partite.
        </p>
      </motion.div>

      <motion.div
        className="overflow-hidden rounded-3xl border-2 border-red-200 bg-white shadow-lg shadow-red-200/60"
        initial={{ opacity: 0, y: 22 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <Link to="/calendario" className="block p-6">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-start gap-3">
              <span className="rounded-2xl bg-red-500 p-3 text-white shadow-lg shadow-red-500/40">
                <CalendarDays size={22} />
              </span>
              <div>
                <h3 className="text-xl font-black text-slate-900">Prenota Partite</h3>
                <p className="mt-1 text-sm text-slate-600">
                  Scegli slot disponibili e invita i tuoi compagni.
                </p>
              </div>
            </div>
            <ChevronRight className="text-red-500" />
          </div>
        </Link>
      </motion.div>

      <motion.div
        className="rounded-3xl border border-slate-200 bg-white p-5 shadow-md"
        initial={{ opacity: 0, y: 24 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.55 }}
      >
        <Link to="/storico" className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <span className="rounded-2xl bg-slate-900 p-3 text-white">
              <History size={20} />
            </span>
            <div>
              <h4 className="text-base font-extrabold text-slate-900">Partite già giocate</h4>
              <p className="text-sm text-slate-600">Controlla cronologia e stato pagamenti.</p>
            </div>
          </div>
          <ChevronRight className="text-slate-500" />
        </Link>
      </motion.div>
    </div>
  );
};

export default UserDashboard;
