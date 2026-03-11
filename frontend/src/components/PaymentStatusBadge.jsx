import { useEffect, useMemo, useRef, useState } from "react";

const colorByStatus = {
  VERDE: "bg-green-500",
  ROSSO: "bg-red-500",
  GIALLO: "bg-yellow-400",
};

const labelByStatus = {
  VERDE: "Pagata",
  ROSSO: "Non pagata",
  GIALLO: "Parziale",
};

const normalizePlayers = (players = []) => {
  const normalized = players.slice(0, 4).map((player) => ({
    nome: player?.nome || "Riservato",
    cognome: player?.cognome || "",
    hasPagato: Boolean(player?.hasPagato),
    metodoPagamento: player?.metodoPagamento || null,
    quotaDaPagare: Number(player?.quotaDaPagare || 0),
    quotaPagata: Number(player?.quotaPagata || 0),
  }));

  while (normalized.length < 4) {
    normalized.push({
      nome: "Posto libero",
      cognome: "",
      hasPagato: false,
      metodoPagamento: null,
      quotaDaPagare: 0,
      quotaPagata: 0,
    });
  }

  return normalized;
};

const formatAmount = (value) => `EUR ${Number(value || 0).toFixed(2)}`;

const PaymentStatusBadge = ({ partita }) => {
  const [open, setOpen] = useState(false);
  const rootRef = useRef(null);

  const stato = partita?.statoGenerale || "ROSSO";
  const players = useMemo(
    () => normalizePlayers(partita?.partecipanti || []),
    [partita?.partecipanti]
  );
  const canOpen = players.length > 0;

  useEffect(() => {
    const onPointerDown = (event) => {
      if (rootRef.current && !rootRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", onPointerDown);
    document.addEventListener("touchstart", onPointerDown);
    return () => {
      document.removeEventListener("mousedown", onPointerDown);
      document.removeEventListener("touchstart", onPointerDown);
    };
  }, []);

  const badgeColor = colorByStatus[stato] || colorByStatus.ROSSO;
  const badgeLabel = labelByStatus[stato] || labelByStatus.ROSSO;

  return (
    <div ref={rootRef} className="relative inline-flex items-center gap-2">
      <button
        type="button"
        className="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-3 py-1 shadow-sm"
        onClick={() => canOpen && setOpen((prev) => !prev)}
        aria-expanded={open}
        aria-label={`Stato pagamento partita: ${badgeLabel}`}
      >
        <span className={`h-3 w-3 rounded-full ${badgeColor}`} />
        <span className="text-xs font-semibold text-slate-700">{badgeLabel}</span>
      </button>

      {open && (
        <div className="absolute left-0 top-12 z-30 w-80 rounded-xl border border-slate-200 bg-white p-3 shadow-lg">
          <div className="mb-2 flex items-center justify-between">
            <h4 className="text-sm font-bold text-slate-800">Stato Pagamenti</h4>
            <span className="text-xs text-slate-500">
              {partita?.numeroPaganti || 0}/{partita?.numeroPartecipanti || 0} paganti
            </span>
          </div>

          <ul className="space-y-2">
            {players.map((player, index) => (
              <li
                key={`${player.nome}-${index}`}
                className="flex items-center justify-between rounded-lg bg-slate-50 px-2 py-2"
              >
                <div className="min-w-0">
                  <p className="truncate text-sm font-medium text-slate-800">
                    {player.nome} {player.cognome}
                  </p>
                  <p className="text-xs text-slate-500">
                    Quota: {formatAmount(player.quotaDaPagare)}
                  </p>
                </div>

                <div className="text-right">
                  <span
                    className={`inline-flex rounded-full px-2 py-0.5 text-[11px] font-semibold ${
                      player.hasPagato
                        ? "bg-green-100 text-green-700"
                        : "bg-red-100 text-red-700"
                    }`}
                  >
                    {player.hasPagato ? "Pagato" : "Da pagare"}
                  </span>
                  <p className="mt-1 text-[11px] text-slate-500">
                    {player.hasPagato
                      ? player.metodoPagamento || "Metodo non indicato"
                      : "N/D"}
                  </p>
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default PaymentStatusBadge;
