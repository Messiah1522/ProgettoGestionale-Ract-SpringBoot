import { motion } from "framer-motion";
import { BarChart3, Trophy } from "lucide-react";
import { useOutletContext } from "react-router";

const pieMock = [
  { label: "Incassi", value: 74, color: "bg-blue-500" },
  { label: "Spese", value: 26, color: "bg-red-500" },
];

const methodsMock = [
  { label: "Contanti", value: 40 },
  { label: "Carta", value: 35 },
  { label: "App", value: 25 },
];

const Statistiche = () => {
  const { isAdmin } = useOutletContext();

  if (!isAdmin) {
    return (
      <section className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm font-medium text-red-700">
        Accesso riservato ad Admin/SuperAdmin.
      </section>
    );
  }

  return (
    <section className="space-y-4">
      <motion.div
        className="rounded-3xl border border-slate-200 bg-white p-5 shadow-lg shadow-slate-200/70"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-xl font-black text-slate-900">Statistiche</h2>
            <p className="text-sm text-slate-600">Dashboard analitica con contenitori Chart.js</p>
          </div>
          <span className="rounded-2xl bg-red-500 p-2 text-white shadow-lg shadow-red-500/40">
            <BarChart3 size={18} />
          </span>
        </div>
      </motion.div>

      <div className="grid gap-4 lg:grid-cols-2">
        <motion.article
          className="rounded-3xl border border-slate-200 bg-white p-5 shadow-md"
          initial={{ opacity: 0, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h3 className="text-base font-black text-slate-900">Incassi vs Spese</h3>
          <p className="mt-1 text-sm text-slate-500">Chart container (Pie)</p>
          <div className="mt-4 space-y-2">
            {pieMock.map((row) => (
              <div key={row.label} className="space-y-1">
                <div className="flex items-center justify-between text-xs font-semibold text-slate-600">
                  <span>{row.label}</span>
                  <span>{row.value}%</span>
                </div>
                <div className="h-2.5 rounded-full bg-slate-100">
                  <div className={`h-full rounded-full ${row.color}`} style={{ width: `${row.value}%` }} />
                </div>
              </div>
            ))}
          </div>
        </motion.article>

        <motion.article
          className="rounded-3xl border border-slate-200 bg-white p-5 shadow-md"
          initial={{ opacity: 0, y: 28 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h3 className="text-base font-black text-slate-900">Metodi di Pagamento</h3>
          <p className="mt-1 text-sm text-slate-500">Chart container (Bar)</p>
          <div className="mt-4 space-y-2">
            {methodsMock.map((row) => (
              <div key={row.label} className="space-y-1">
                <div className="flex items-center justify-between text-xs font-semibold text-slate-600">
                  <span>{row.label}</span>
                  <span>{row.value}%</span>
                </div>
                <div className="h-2.5 rounded-full bg-slate-100">
                  <div className="h-full rounded-full bg-sky-500" style={{ width: `${row.value}%` }} />
                </div>
              </div>
            ))}
          </div>
        </motion.article>
      </div>

      <motion.article
        className="rounded-3xl border border-slate-200 bg-white p-5 shadow-md"
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <h3 className="mb-3 flex items-center gap-2 text-base font-black text-slate-900">
          <Trophy className="text-red-500" size={18} />
          Top Spender
        </h3>
        <div className="grid gap-2 md:grid-cols-2">
          <div className="rounded-2xl border border-slate-200 bg-slate-50 p-3 text-sm">
            <p className="text-xs font-semibold uppercase text-slate-500">Giorno</p>
            <p className="mt-1 font-black text-slate-900">Marta Rossi</p>
            <p className="text-slate-600">EUR 84.00</p>
          </div>
          <div className="rounded-2xl border border-slate-200 bg-slate-50 p-3 text-sm">
            <p className="text-xs font-semibold uppercase text-slate-500">Mese</p>
            <p className="mt-1 font-black text-slate-900">Andrea Bianchi</p>
            <p className="text-slate-600">EUR 640.00</p>
          </div>
        </div>
      </motion.article>
    </section>
  );
};

export default Statistiche;
