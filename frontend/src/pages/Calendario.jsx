import { useCallback, useEffect, useMemo, useState } from "react";
import { useOutletContext } from "react-router";
import InvitePlayersModal from "../components/InvitePlayersModal";
import {
  createPrenotazione,
  getCampi,
  getPartiteGiorno,
  getSlotOccupati,
  searchAmici,
} from "../lib/api";

const SLOT_DURATION_MINUTES = 90;

const toDateInputValue = (date) => {
  const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
  return local.toISOString().slice(0, 10);
};

const toMonthKey = (date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}`;

const formatDayLabel = (dateString) => {
  const date = new Date(`${dateString}T00:00:00`);
  return date.toLocaleDateString("it-IT", {
    weekday: "long",
    day: "2-digit",
    month: "long",
  });
};

const addMinutes = (timeString, delta) => {
  const [h, m] = timeString.split(":").map(Number);
  const total = h * 60 + m + delta;
  const nextH = Math.floor(total / 60);
  const nextM = total % 60;
  return `${String(nextH).padStart(2, "0")}:${String(nextM).padStart(2, "0")}`;
};

const generateSlots = () => {
  const slots = [];
  for (let h = 7; h <= 21; h++) {
    for (const m of [0, 30]) {
      if (h === 21 && m > 30) continue;
      const start = `${String(h).padStart(2, "0")}:${String(m).padStart(2, "0")}`;
      const end = addMinutes(start, SLOT_DURATION_MINUTES);
      if (end > "23:00") continue;
      slots.push({ start, end });
    }
  }
  return slots;
};

const isOverlap = (startA, endA, startB, endB) => {
  const toMin = (value) => {
    const [h, m] = value.split(":").map(Number);
    return h * 60 + m;
  };
  const aStart = toMin(startA);
  const aEnd = toMin(endA);
  const bStart = toMin(startB);
  const bEnd = toMin(endB);
  return aStart < bEnd && bStart < aEnd;
};

const normalizeTime = (isoDateTime) => {
  if (!isoDateTime) return "00:00";
  return String(isoDateTime).slice(11, 16);
};

const Calendario = () => {
  const { viewerRole, viewerUserId, isAdmin } = useOutletContext();

  const [selectedDay, setSelectedDay] = useState(() => toDateInputValue(new Date()));
  const [campi, setCampi] = useState([]);
  const [selectedCampoId, setSelectedCampoId] = useState("");
  const [occupiedSlots, setOccupiedSlots] = useState([]);
  const [partite, setPartite] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const slots = useMemo(() => generateSlots(), []);

  useEffect(() => {
    const loadCampi = async () => {
      try {
        const data = await getCampi();
        setCampi(data || []);
        if (data?.length) {
          setSelectedCampoId(String(data[0].id));
        }
      } catch (err) {
        setError(`Errore campi: ${err.message}`);
      }
    };

    loadCampi();
  }, []);

  const refreshData = async () => {
    if (!selectedCampoId) return;
    setLoading(true);
    setError("");

    try {
      const [slotsData, partiteData] = await Promise.all([
        getSlotOccupati(Number(selectedCampoId), selectedDay),
        getPartiteGiorno({
          giorno: selectedDay,
          campoId: Number(selectedCampoId),
          viewerUserId,
          viewerRole,
        }),
      ]);

      setOccupiedSlots(slotsData || []);
      setPartite(partiteData || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refreshData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDay, selectedCampoId, viewerRole, viewerUserId]);

  const currentMonth = toMonthKey(new Date());
  const nextMonthDate = new Date();
  nextMonthDate.setMonth(nextMonthDate.getMonth() + 1);
  const nextMonth = toMonthKey(nextMonthDate);

  const canSelectDate = (targetDate) => {
    if (isAdmin) return true;
    const key = toMonthKey(new Date(`${targetDate}T00:00:00`));
    return key === currentMonth || key === nextMonth;
  };

  const changeDay = (delta) => {
    const base = new Date(`${selectedDay}T00:00:00`);
    base.setDate(base.getDate() + delta);
    const target = toDateInputValue(base);

    if (!canSelectDate(target)) {
      setNotice("Come USER puoi prenotare solo nel mese corrente o successivo.");
      return;
    }

    setSelectedDay(target);
  };

  const occupiedForSlot = (slot) =>
    occupiedSlots.some((occupied) =>
      isOverlap(
        slot.start,
        slot.end,
        normalizeTime(occupied.dataOraInizio),
        normalizeTime(occupied.dataOraFine)
      )
    );

  const selectedCampo = campi.find((campo) => String(campo.id) === String(selectedCampoId));

  const handleOpenBooking = (slot) => {
    setNotice("");
    const startDateTime = `${selectedDay}T${slot.start}:00`;
    if (!isAdmin && !canSelectDate(selectedDay)) {
      setNotice("Data non prenotabile per il ruolo selezionato.");
      return;
    }

    setSelectedSlot({
      dataOraInizio: startDateTime,
      dataOraFine: `${selectedDay}T${slot.end}:00`,
      start: slot.start,
      end: slot.end,
    });
    setModalOpen(true);
  };

  const handleConfirmBooking = async (invitatiIds) => {
    if (!selectedSlot || !selectedCampo) return;

    try {
      const tariffa = Number(selectedCampo.tariffaOraria || 40);
      const costoTotale = Number((tariffa * 1.5).toFixed(2));

      await createPrenotazione({
        viewerUserId,
        viewerRole,
        requestBody: {
          campoId: Number(selectedCampoId),
          dataOraInizio: selectedSlot.dataOraInizio,
          dataOraFine: selectedSlot.dataOraFine,
          costoTotale,
          note: "Prenotazione da webapp React",
          invitatiIds,
        },
      });

      setNotice("Prenotazione creata con successo.");
      setModalOpen(false);
      setSelectedSlot(null);
      await refreshData();
    } catch (err) {
      setNotice(`Errore prenotazione: ${err.message}`);
    }
  };

  const handleSearchUsers = useCallback(
    async (query, excludeUserId) => searchAmici({ query, excludeUserId, limit: 8 }),
    []
  );

  return (
    <section className="space-y-4">
      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <div className="mb-3 flex items-center justify-between">
          <h2 className="text-lg font-bold text-slate-800">Calendario Prenotazioni</h2>
          <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600">
            {isAdmin ? "Vista Admin" : "Vista User"}
          </span>
        </div>

        <div className="grid gap-3 md:grid-cols-3">
          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={() => changeDay(-1)}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm font-semibold"
            >
              ◀
            </button>
            <input
              type="date"
              value={selectedDay}
              onChange={(event) => {
                if (!canSelectDate(event.target.value)) {
                  setNotice("Data non consentita per ROLE_USER.");
                  return;
                }
                setSelectedDay(event.target.value);
              }}
              className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
            />
            <button
              type="button"
              onClick={() => changeDay(1)}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm font-semibold"
            >
              ▶
            </button>
          </div>

          <select
            value={selectedCampoId}
            onChange={(event) => setSelectedCampoId(event.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          >
            {campi.map((campo) => (
              <option key={campo.id} value={campo.id}>
                {campo.nome}
              </option>
            ))}
          </select>

          <div className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-600">
            {formatDayLabel(selectedDay)}
          </div>
        </div>

        {notice && (
          <p className="mt-3 rounded-lg bg-amber-50 px-3 py-2 text-sm font-medium text-amber-800">
            {notice}
          </p>
        )}
        {error && (
          <p className="mt-3 rounded-lg bg-red-50 px-3 py-2 text-sm font-medium text-red-700">
            {error}
          </p>
        )}
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <h3 className="mb-3 text-sm font-bold uppercase tracking-wide text-slate-500">
          Slot Disponibili ({selectedCampo?.nome || "-"})
        </h3>

        {loading ? (
          <p className="text-sm text-slate-500">Caricamento slot...</p>
        ) : (
          <div className="grid grid-cols-2 gap-2 md:grid-cols-4">
            {slots.map((slot) => {
              const occupied = occupiedForSlot(slot);
              return (
                <button
                  key={`${slot.start}-${slot.end}`}
                  type="button"
                  disabled={occupied}
                  onClick={() => handleOpenBooking(slot)}
                  className={`rounded-xl px-3 py-3 text-left text-sm font-semibold transition ${
                    occupied
                      ? "cursor-not-allowed border border-slate-200 bg-slate-100 text-slate-400"
                      : "border border-blue-200 bg-blue-50 text-blue-800 hover:bg-blue-100"
                  }`}
                >
                  <div>{slot.start}</div>
                  <div className="text-xs font-medium">{slot.end}</div>
                </button>
              );
            })}
          </div>
        )}
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <h3 className="mb-3 text-sm font-bold uppercase tracking-wide text-slate-500">
          Partite Del Giorno
        </h3>

        {!partite.length ? (
          <p className="text-sm text-slate-500">Nessuna partita trovata per questo giorno.</p>
        ) : (
          <div className="space-y-2">
            {partite.map((partita) => (
              <article
                key={partita.partitaId}
                className="rounded-xl border border-slate-200 bg-slate-50 p-3"
              >
                <div className="flex items-center justify-between">
                  <p className="font-semibold text-slate-800">
                    {normalizeTime(partita.dataOraInizio)} - {normalizeTime(partita.dataOraFine)}
                  </p>
                  <span className="rounded-full bg-slate-200 px-2 py-0.5 text-xs font-semibold text-slate-700">
                    {partita.stato}
                  </span>
                </div>

                <ul className="mt-2 space-y-1 text-sm text-slate-700">
                  {partita.partecipanti?.map((p, index) => (
                    <li key={`${partita.partitaId}-${index}`}>
                      {p.nome} {p.cognome} {p.confermato ? "" : "(in attesa)"}
                    </li>
                  ))}
                </ul>
              </article>
            ))}
          </div>
        )}
      </div>

      <InvitePlayersModal
        isOpen={modalOpen}
        selectedSlot={selectedSlot}
        onClose={() => setModalOpen(false)}
        onConfirm={handleConfirmBooking}
        currentUserId={viewerUserId}
        searchUsers={handleSearchUsers}
      />
    </section>
  );
};

export default Calendario;
