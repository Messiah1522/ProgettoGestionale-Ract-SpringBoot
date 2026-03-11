import { useState } from "react";
import { useLocation, useNavigate } from "react-router";
import { motion } from "framer-motion";
import { LogIn, ShieldCheck } from "lucide-react";
import { useAuth } from "../context/AuthContext";

const demoAccounts = [
  { label: "USER Demo", identifier: "user@appiapadel.local", password: "User123!" },
  { label: "ADMIN Demo", identifier: "admin@appiapadel.local", password: "Admin123!" },
  {
    label: "SUPERADMIN Demo",
    identifier: "superadmin@appiapadel.local",
    password: "Super123!",
  },
];

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();
  const from = location.state?.from || "/dashboard";

  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const submit = async (idValue, passValue) => {
    setLoading(true);
    setError("");
    try {
      await login(idValue.trim(), passValue);
      navigate(from, { replace: true });
    } catch (err) {
      setError(err.message || "Credenziali non valide.");
    } finally {
      setLoading(false);
    }
  };

  const onSubmit = async (event) => {
    event.preventDefault();
    if (!identifier.trim() || !password) {
      setError("Inserisci Codice Fiscale/Email e Password.");
      return;
    }
    await submit(identifier, password);
  };

  return (
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-white px-4 py-8">
      <div className="absolute inset-0 bg-gradient-to-br from-blue-200/70 via-white to-sky-100/80" />

      <motion.section
        initial={{ opacity: 0, y: 24 }}
        animate={{ opacity: 1, y: 0 }}
        className="relative z-10 w-full max-w-md rounded-3xl border border-white/60 bg-white/55 p-6 shadow-2xl shadow-blue-200/70 backdrop-blur-xl"
      >
        <div className="mb-5">
          <p className="inline-flex items-center gap-2 rounded-full border border-blue-200 bg-blue-50 px-3 py-1 text-[11px] font-bold uppercase tracking-[0.18em] text-blue-700">
            <ShieldCheck size={14} />
            Accesso Sicuro
          </p>
          <h1 className="mt-3 text-3xl font-black tracking-tight text-slate-900">
            <span className="text-slate-900">Appia</span>{" "}
            <span className="text-red-500">Padel</span>
          </h1>
          <p className="mt-1 text-sm text-slate-600">Login con Codice Fiscale + Password</p>
        </div>

        <form onSubmit={onSubmit} className="space-y-3">
          <label className="block">
            <span className="mb-1 block text-xs font-semibold uppercase tracking-wide text-slate-500">
              Codice Fiscale o Email
            </span>
            <input
              value={identifier}
              onChange={(event) => setIdentifier(event.target.value)}
              placeholder="RSSMRA85B15H501P"
              className="w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2.5 text-sm text-slate-800 outline-none ring-red-400 transition focus:ring"
              autoComplete="username"
            />
          </label>

          <label className="block">
            <span className="mb-1 block text-xs font-semibold uppercase tracking-wide text-slate-500">
              Password
            </span>
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Inserisci password"
              className="w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2.5 text-sm text-slate-800 outline-none ring-red-400 transition focus:ring"
              autoComplete="current-password"
            />
          </label>

          {error && (
            <p className="rounded-xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-700">
              {error}
            </p>
          )}

          <motion.button
            type="submit"
            disabled={loading}
            whileTap={{ scale: 0.98 }}
            className="mt-1 inline-flex w-full items-center justify-center gap-2 rounded-xl bg-red-500 px-4 py-3 text-sm font-black text-white shadow-lg shadow-red-500/50 transition hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-red-300"
          >
            <LogIn size={16} />
            {loading ? "Accesso..." : "Accedi"}
          </motion.button>
        </form>

        <div className="mt-5 space-y-2">
          <p className="text-[11px] font-bold uppercase tracking-[0.16em] text-slate-500">
            Accessi Demo
          </p>
          {demoAccounts.map((account) => (
            <motion.button
              key={account.label}
              type="button"
              whileTap={{ scale: 0.98 }}
              onClick={() => submit(account.identifier, account.password)}
              className="w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 text-left text-sm font-semibold text-slate-700 hover:border-red-200 hover:bg-red-50"
            >
              {account.label}
            </motion.button>
          ))}
        </div>
      </motion.section>
    </div>
  );
};

export default Login;
