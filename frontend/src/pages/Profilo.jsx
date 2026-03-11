import { useOutletContext } from "react-router";

const Profilo = () => {
  const { viewerRole, viewerUserId, isAdmin, session } = useOutletContext();

  return (
    <section className="space-y-4">
      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <h2 className="text-lg font-bold text-slate-800">Profilo Sessione</h2>
        <p className="mt-2 text-sm text-slate-600">
          Sessione attiva tramite login backend con ruolo assegnato.
        </p>

        <dl className="mt-4 grid gap-2 text-sm">
          <div className="rounded-lg bg-slate-50 px-3 py-2">
            <dt className="font-semibold text-slate-500">Nome</dt>
            <dd className="text-slate-800">
              {session?.nome} {session?.cognome}
            </dd>
          </div>
          <div className="rounded-lg bg-slate-50 px-3 py-2">
            <dt className="font-semibold text-slate-500">Email</dt>
            <dd className="text-slate-800">{session?.email || "-"}</dd>
          </div>
          <div className="rounded-lg bg-slate-50 px-3 py-2">
            <dt className="font-semibold text-slate-500">Ruolo</dt>
            <dd className="text-slate-800">{viewerRole}</dd>
          </div>
          <div className="rounded-lg bg-slate-50 px-3 py-2">
            <dt className="font-semibold text-slate-500">Viewer User ID</dt>
            <dd className="text-slate-800">{viewerUserId}</dd>
          </div>
          <div className="rounded-lg bg-slate-50 px-3 py-2">
            <dt className="font-semibold text-slate-500">Permessi Admin</dt>
            <dd className="text-slate-800">{isAdmin ? "Attivi" : "Non attivi"}</dd>
          </div>
        </dl>
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <h3 className="text-sm font-bold uppercase tracking-wide text-slate-500">Stato Moduli</h3>
        <ul className="mt-3 space-y-2 text-sm text-slate-700">
          <li className="rounded-lg bg-green-50 px-3 py-2">Prenotazioni: attivo (slot, inviti, scadenze)</li>
          <li className="rounded-lg bg-green-50 px-3 py-2">Pagamenti partita: attivo (badge colori + dettaglio)</li>
          <li className="rounded-lg bg-green-50 px-3 py-2">Cassa base: attiva (scarico magazzino)</li>
          <li className="rounded-lg bg-amber-50 px-3 py-2">JWT/RBAC avanzato: step successivo</li>
        </ul>
      </div>
    </section>
  );
};

export default Profilo;
