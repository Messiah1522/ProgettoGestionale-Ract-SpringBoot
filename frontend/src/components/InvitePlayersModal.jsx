import { useEffect, useMemo, useState } from "react";

const formatDateTime = (value) => {
  if (!value) return "";
  const date = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(date.getTime())) return "";
  return date.toLocaleString("it-IT", {
    day: "2-digit",
    month: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const InvitePlayersModal = ({
  isOpen,
  selectedSlot,
  onClose,
  onConfirm,
  searchUsers,
  currentUserId,
}) => {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [selectedPlayers, setSelectedPlayers] = useState([]);
  const [loading, setLoading] = useState(false);

  const scadenzaInviti = useMemo(() => {
    const date = new Date();
    date.setHours(date.getHours() + 3);
    return date;
  }, [isOpen]);

  useEffect(() => {
    if (!isOpen) {
      setQuery("");
      setSuggestions((prev) => (prev.length ? [] : prev));
      setSelectedPlayers((prev) => (prev.length ? [] : prev));
      setLoading(false);
      return;
    }

    if (!query || query.trim().length < 2) {
      setSuggestions((prev) => (prev.length ? [] : prev));
      setLoading(false);
      return;
    }

    let cancelled = false;
    setLoading(true);

    const timeout = setTimeout(async () => {
      try {
        const users = await searchUsers(query.trim(), currentUserId);
        if (!cancelled) {
          const selectedIds = new Set(selectedPlayers.map((player) => player.id));
          setSuggestions(
            (users || []).filter((user) => !selectedIds.has(user.id)).slice(0, 8)
          );
        }
      } catch {
        if (!cancelled) {
          setSuggestions([]);
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }, 250);

    return () => {
      cancelled = true;
      clearTimeout(timeout);
    };
  }, [query, searchUsers, currentUserId, selectedPlayers, isOpen]);

  const handleSelectPlayer = (user) => {
    if (!user || selectedPlayers.length >= 3) return;
    if (selectedPlayers.some((player) => player.id === user.id)) return;
    setSelectedPlayers((prev) => [...prev, user]);
    setQuery("");
    setSuggestions([]);
  };

  const removePlayer = (id) => {
    setSelectedPlayers((prev) => prev.filter((player) => player.id !== id));
  };

  const handleConfirm = () => {
    onConfirm?.(selectedPlayers.map((player) => player.id));
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 bg-slate-900/50">
      <div
        className="absolute inset-0"
        onClick={onClose}
        aria-label="Chiudi modal inviti"
      />

      <div className="absolute bottom-0 left-0 right-0 rounded-t-2xl bg-white p-4 shadow-2xl md:mx-auto md:bottom-8 md:max-w-xl md:rounded-2xl">
        <div className="mb-4 flex items-start justify-between gap-3">
          <div>
            <h3 className="text-lg font-bold text-slate-900">Invita i giocatori</h3>
            <p className="text-sm text-slate-500">
              Slot: {formatDateTime(selectedSlot?.dataOraInizio)} -{" "}
              {formatDateTime(selectedSlot?.dataOraFine)}
            </p>
            <p className="mt-1 text-xs font-medium text-amber-700">
              In attesa risposte fino alle {formatDateTime(scadenzaInviti)}
            </p>
          </div>
          <button
            type="button"
            className="rounded-md border border-slate-200 px-2 py-1 text-xs text-slate-600"
            onClick={onClose}
          >
            Chiudi
          </button>
        </div>

        <div className="mb-2">
          <label className="mb-1 block text-xs font-semibold uppercase tracking-wide text-slate-600">
            Cerca amici (max 3)
          </label>
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Digita nome o cognome"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none ring-blue-500 focus:ring"
          />
        </div>

        {loading && <p className="text-xs text-slate-500">Ricerca in corso...</p>}

        {!loading && suggestions.length > 0 && (
          <ul className="mb-3 max-h-40 overflow-y-auto rounded-lg border border-slate-200">
            {suggestions.map((user) => (
              <li key={user.id}>
                <button
                  type="button"
                  className="flex w-full items-center justify-between px-3 py-2 text-left text-sm hover:bg-slate-50"
                  onClick={() => handleSelectPlayer(user)}
                >
                  <span>
                    {user.nome} {user.cognome}
                  </span>
                  <span className="text-xs text-blue-600">Aggiungi</span>
                </button>
              </li>
            ))}
          </ul>
        )}

        <div className="mb-4 flex flex-wrap gap-2">
          {selectedPlayers.length === 0 && (
            <span className="rounded-full bg-slate-100 px-3 py-1 text-xs text-slate-500">
              Nessun invitato selezionato
            </span>
          )}
          {selectedPlayers.map((player) => (
            <span
              key={player.id}
              className="inline-flex items-center gap-2 rounded-full bg-blue-50 px-3 py-1 text-xs text-blue-700"
            >
              {player.nome} {player.cognome}
              <button
                type="button"
                className="font-bold text-blue-700"
                onClick={() => removePlayer(player.id)}
                aria-label={`Rimuovi ${player.nome}`}
              >
                x
              </button>
            </span>
          ))}
        </div>

        <div className="flex items-center justify-end gap-2">
          <button
            type="button"
            onClick={onClose}
            className="rounded-lg border border-slate-300 px-4 py-2 text-sm text-slate-700"
          >
            Annulla
          </button>
          <button
            type="button"
            onClick={handleConfirm}
            className="rounded-lg bg-blue-700 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-800"
          >
            Conferma Prenotazione
          </button>
        </div>
      </div>
    </div>
  );
};

export default InvitePlayersModal;
