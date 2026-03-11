package com.progetto.gestionale.service.padel.impl;

import com.progetto.gestionale.dto.padel.InvitoPrenotazioneDTO;
import com.progetto.gestionale.dto.padel.PartecipanteCalendarioDTO;
import com.progetto.gestionale.dto.padel.PartitaCalendarioDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneAdminCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneDTO;
import com.progetto.gestionale.dto.padel.SlotOccupatoDTO;
import com.progetto.gestionale.dto.padel.UtenteLiteDTO;
import com.progetto.gestionale.entity.padel.CampoPadel;
import com.progetto.gestionale.entity.padel.InvitoPartita;
import com.progetto.gestionale.entity.padel.PartecipazionePartita;
import com.progetto.gestionale.entity.padel.Partita;
import com.progetto.gestionale.entity.padel.Utente;
import com.progetto.gestionale.entity.padel.enums.OriginePartecipante;
import com.progetto.gestionale.entity.padel.enums.StatoInvitoPartita;
import com.progetto.gestionale.entity.padel.enums.StatoPartita;
import com.progetto.gestionale.repository.padel.CampoPadelRepository;
import com.progetto.gestionale.repository.padel.InvitoPartitaRepository;
import com.progetto.gestionale.repository.padel.PartecipazionePartitaRepository;
import com.progetto.gestionale.repository.padel.PartitaPadelRepository;
import com.progetto.gestionale.repository.padel.UtentePadelRepository;
import com.progetto.gestionale.service.padel.AdminNotificationService;
import com.progetto.gestionale.service.padel.PrenotazioneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {

    private static final Logger log = LoggerFactory.getLogger(PrenotazioneServiceImpl.class);
    private static final int MAX_GIOCATORI_PARTITA = 4;
    private static final int MAX_INVITI = 3;

    private final PartitaPadelRepository partitaPadelRepository;
    private final CampoPadelRepository campoPadelRepository;
    private final UtentePadelRepository utentePadelRepository;
    private final InvitoPartitaRepository invitoPartitaRepository;
    private final PartecipazionePartitaRepository partecipazionePartitaRepository;
    private final AdminNotificationService adminNotificationService;

    public PrenotazioneServiceImpl(
        PartitaPadelRepository partitaPadelRepository,
        CampoPadelRepository campoPadelRepository,
        UtentePadelRepository utentePadelRepository,
        InvitoPartitaRepository invitoPartitaRepository,
        PartecipazionePartitaRepository partecipazionePartitaRepository,
        AdminNotificationService adminNotificationService
    ) {
        this.partitaPadelRepository = partitaPadelRepository;
        this.campoPadelRepository = campoPadelRepository;
        this.utentePadelRepository = utentePadelRepository;
        this.invitoPartitaRepository = invitoPartitaRepository;
        this.partecipazionePartitaRepository = partecipazionePartitaRepository;
        this.adminNotificationService = adminNotificationService;
    }

    @Override
    @Transactional
    public PrenotazioneDTO creaPrenotazioneUtente(PrenotazioneCreateRequestDTO request, Long viewerUserId, String viewerRole) {
        if (viewerUserId == null) {
            throw new IllegalArgumentException("viewerUserId obbligatorio per creare una prenotazione utente.");
        }

        boolean isAdmin = isAdminRole(viewerRole);
        if (!isAdmin && !isUserRole(viewerRole)) {
            throw new IllegalArgumentException("Ruolo non autorizzato alla prenotazione.");
        }

        Utente organizzatore = getUtenteAttivo(viewerUserId);
        return creaPrenotazioneInterna(request, organizzatore, !isAdmin);
    }

    @Override
    @Transactional
    public PrenotazioneDTO creaPrenotazionePerContoDiAdmin(PrenotazioneAdminCreateRequestDTO request, String viewerRole) {
        requireAdminRole(viewerRole);

        if (request == null || request.getUtenteId() == null) {
            throw new IllegalArgumentException("utenteId obbligatorio per creare prenotazioni per conto di terzi.");
        }

        Utente organizzatore = getUtenteAttivo(request.getUtenteId());
        return creaPrenotazioneInterna(request, organizzatore, false);
    }

    @Override
    @Transactional
    public PrenotazioneDTO aggiungiGiocatoreAdmin(Long partitaId, Long utenteId, String viewerRole) {
        requireAdminRole(viewerRole);

        if (partitaId == null || utenteId == null) {
            throw new IllegalArgumentException("partitaId e utenteId sono obbligatori.");
        }

        Partita partita = getPartita(partitaId);
        Utente utente = getUtenteAttivo(utenteId);

        PartecipazionePartita partecipazioneEsistente = partecipazionePartitaRepository
            .findByPartitaIdAndUtenteId(partitaId, utenteId)
            .orElse(null);

        if (partecipazioneEsistente != null && Boolean.TRUE.equals(partecipazioneEsistente.getConfermato())) {
            throw new IllegalArgumentException("Il giocatore è già presente nella partita.");
        }

        long giocatoriConfermati = partita.getPartecipanti().stream()
            .filter(p -> Boolean.TRUE.equals(p.getConfermato()))
            .count();

        if (partecipazioneEsistente == null && giocatoriConfermati >= MAX_GIOCATORI_PARTITA) {
            throw new IllegalArgumentException("Impossibile aggiungere il giocatore: partita già completa (4/4).");
        }

        if (partecipazioneEsistente == null) {
            PartecipazionePartita partecipazioneNuova = buildPartecipazione(utente, OriginePartecipante.AGGIUNTO_DA_ADMIN, true);
            partita.addPartecipante(partecipazioneNuova);
        } else {
            partecipazioneEsistente.setConfermato(true);
            partecipazioneEsistente.setOriginePartecipante(OriginePartecipante.AGGIUNTO_DA_ADMIN);
        }

        aggiornaStatoPartitaDaInviti(partita);
        ricalcolaQuotePartita(partita);

        Partita salvata = partitaPadelRepository.save(partita);
        return toPrenotazioneDTO(salvata);
    }

    @Override
    @Transactional
    public PrenotazioneDTO rispondiInvito(Long invitoId, Long destinatarioId, boolean accetta) {
        if (invitoId == null || destinatarioId == null) {
            throw new IllegalArgumentException("invitoId e destinatarioId sono obbligatori.");
        }

        InvitoPartita invito = invitoPartitaRepository.findByIdAndDestinatarioId(invitoId, destinatarioId)
            .orElseThrow(() -> new IllegalArgumentException("Invito non trovato."));

        if (invito.getStato() != StatoInvitoPartita.IN_ATTESA) {
            throw new IllegalArgumentException("Invito già gestito con stato: " + invito.getStato());
        }

        LocalDateTime now = LocalDateTime.now();
        Partita partita = getPartita(invito.getPartita().getId());

        PartecipazionePartita partecipazione = partecipazionePartitaRepository
            .findByPartitaIdAndUtenteId(partita.getId(), destinatarioId)
            .orElse(null);

        boolean expired = invito.getDataScadenza() != null && now.isAfter(invito.getDataScadenza());

        if (expired) {
            invito.setStato(StatoInvitoPartita.SCADUTO);
            if (partecipazione != null) {
                partecipazione.setConfermato(false);
            }
        } else if (accetta) {
            invito.setStato(StatoInvitoPartita.ACCETTATO);
            if (partecipazione != null) {
                partecipazione.setConfermato(true);
            }
        } else {
            invito.setStato(StatoInvitoPartita.RIFIUTATO);
            if (partecipazione != null) {
                partecipazione.setConfermato(false);
            }
        }

        invito.setDataRisposta(now);
        invitoPartitaRepository.save(invito);

        aggiornaStatoPartitaDaInviti(partita);
        ricalcolaQuotePartita(partita);

        Partita salvata = partitaPadelRepository.save(partita);
        return toPrenotazioneDTO(salvata);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotOccupatoDTO> getSlotOccupati(Long campoId, LocalDate giorno) {
        if (campoId == null || giorno == null) {
            throw new IllegalArgumentException("campoId e giorno sono obbligatori.");
        }

        LocalDateTime start = giorno.atStartOfDay();
        LocalDateTime end = giorno.atTime(LocalTime.MAX);

        List<Partita> partite = partitaPadelRepository
            .findByCampoIdAndDataOraInizioBetweenOrderByDataOraInizioAsc(campoId, start, end);

        List<SlotOccupatoDTO> slots = new ArrayList<>();
        for (Partita partita : partite) {
            if (partita.getStato() == StatoPartita.CANCELLATA) {
                continue;
            }
            SlotOccupatoDTO slot = new SlotOccupatoDTO();
            slot.setPartitaId(partita.getId());
            slot.setDataOraInizio(partita.getDataOraInizio());
            slot.setDataOraFine(partita.getDataOraFine());
            slots.add(slot);
        }

        return slots;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartitaCalendarioDTO> getPartiteGiorno(
        LocalDate giorno,
        Long campoId,
        Long viewerUserId,
        String viewerRole
    ) {
        if (giorno == null) {
            throw new IllegalArgumentException("giorno obbligatorio.");
        }

        LocalDateTime start = giorno.atStartOfDay();
        LocalDateTime end = giorno.atTime(LocalTime.MAX);
        boolean isAdmin = isAdminRole(viewerRole);

        List<Partita> partite;
        if (campoId != null) {
            partite = partitaPadelRepository.findByCampoIdAndDataOraInizioBetweenOrderByDataOraInizioAsc(campoId, start, end);
        } else {
            partite = partitaPadelRepository.findAll().stream()
                .filter(p -> p.getDataOraInizio() != null)
                .filter(p -> !p.getDataOraInizio().isBefore(start) && !p.getDataOraInizio().isAfter(end))
                .sorted(Comparator.comparing(Partita::getDataOraInizio))
                .toList();
        }

        List<PartitaCalendarioDTO> result = new ArrayList<>();

        for (Partita partita : partite) {
            if (partita.getStato() == StatoPartita.CANCELLATA) {
                continue;
            }

            PartitaCalendarioDTO dto = new PartitaCalendarioDTO();
            dto.setPartitaId(partita.getId());
            dto.setDataOraInizio(partita.getDataOraInizio());
            dto.setDataOraFine(partita.getDataOraFine());
            dto.setStato(partita.getStato());
            if (partita.getCampo() != null) {
                dto.setCampoId(partita.getCampo().getId());
                dto.setCampoNome(partita.getCampo().getNome());
            }

            List<PartecipanteCalendarioDTO> partecipanti = new ArrayList<>();
            if (partita.getPartecipanti() != null) {
                for (PartecipazionePartita partecipazione : partita.getPartecipanti()) {
                    PartecipanteCalendarioDTO partecipante = new PartecipanteCalendarioDTO();
                    partecipante.setConfermato(Boolean.TRUE.equals(partecipazione.getConfermato()));

                    if (partecipazione.getUtente() != null) {
                        Long utenteId = partecipazione.getUtente().getId();
                        partecipante.setUtenteId(utenteId);

                        boolean showFullIdentity = isAdmin || (viewerUserId != null && viewerUserId.equals(utenteId));
                        if (showFullIdentity) {
                            partecipante.setNome(partecipazione.getUtente().getNome());
                            partecipante.setCognome(partecipazione.getUtente().getCognome());
                        } else {
                            partecipante.setNome("Riservato");
                            partecipante.setCognome("");
                        }
                    } else {
                        partecipante.setNome("Riservato");
                        partecipante.setCognome("");
                    }

                    partecipanti.add(partecipante);
                }
            }

            dto.setPartecipanti(partecipanti);
            result.add(dto);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteLiteDTO> cercaGiocatori(String term, Long excludeUserId, Integer limit) {
        if (term == null || term.trim().isBlank()) {
            return List.of();
        }

        int safeLimit = (limit == null) ? 10 : Math.max(1, Math.min(limit, 30));

        List<Utente> utenti = utentePadelRepository.searchAttivi(term.trim(), PageRequest.of(0, safeLimit));
        List<UtenteLiteDTO> risultati = new ArrayList<>();

        for (Utente utente : utenti) {
            if (excludeUserId != null && excludeUserId.equals(utente.getId())) {
                continue;
            }
            UtenteLiteDTO dto = new UtenteLiteDTO();
            dto.setId(utente.getId());
            dto.setNome(utente.getNome());
            dto.setCognome(utente.getCognome());
            risultati.add(dto);
        }

        return risultati;
    }

    @Override
    @Scheduled(fixedDelayString = "${app.padel.inviti.expire-check-ms:60000}")
    @Transactional
    public void processaInvitiScaduti() {
        LocalDateTime now = LocalDateTime.now();
        List<InvitoPartita> invitiScaduti = invitoPartitaRepository
            .findByStatoAndDataScadenzaBefore(StatoInvitoPartita.IN_ATTESA, now);

        if (invitiScaduti.isEmpty()) {
            return;
        }

        Set<Long> partiteDaAggiornare = new LinkedHashSet<>();

        for (InvitoPartita invito : invitiScaduti) {
            invito.setStato(StatoInvitoPartita.SCADUTO);
            invito.setDataRisposta(now);
            partiteDaAggiornare.add(invito.getPartita().getId());

            partecipazionePartitaRepository.findByPartitaIdAndUtenteId(
                invito.getPartita().getId(),
                invito.getDestinatario().getId()
            ).ifPresent(partecipazione -> partecipazione.setConfermato(false));
        }

        invitoPartitaRepository.saveAll(invitiScaduti);

        for (Long partitaId : partiteDaAggiornare) {
            Partita partita = getPartita(partitaId);
            aggiornaStatoPartitaDaInviti(partita);
            ricalcolaQuotePartita(partita);
            partitaPadelRepository.save(partita);
        }

        log.info("Gestiti {} inviti scaduti automaticamente.", invitiScaduti.size());
    }

    private PrenotazioneDTO creaPrenotazioneInterna(
        PrenotazioneCreateRequestDTO request,
        Utente organizzatore,
        boolean enforceUserDateWindow
    ) {
        validateRequestBase(request);

        if (enforceUserDateWindow) {
            validateFinestraPrenotazioneUser(request.getDataOraInizio());
        }

        CampoPadel campo = campoPadelRepository.findById(request.getCampoId())
            .orElseThrow(() -> new IllegalArgumentException("Campo non trovato con id: " + request.getCampoId()));

        boolean slotOccupato = partitaPadelRepository
            .existsByCampoIdAndDataOraInizioLessThanAndDataOraFineGreaterThanAndStatoNot(
                campo.getId(),
                request.getDataOraFine(),
                request.getDataOraInizio(),
                StatoPartita.CANCELLATA
            );

        if (slotOccupato) {
            throw new IllegalArgumentException("Campo già occupato nello slot richiesto.");
        }

        List<Long> invitatiIds = sanitizeInvitati(request.getInvitatiIds(), organizzatore.getId());

        Partita partita = new Partita();
        partita.setCampo(campo);
        partita.setCreataDa(organizzatore);
        partita.setDataOraInizio(request.getDataOraInizio());
        partita.setDataOraFine(request.getDataOraFine());
        partita.setCostoTotale(defaultIfNull(request.getCostoTotale()));
        partita.setNote(request.getNote());
        partita.setStato(invitatiIds.isEmpty() ? StatoPartita.CONFERMATA : StatoPartita.IN_ATTESA_CONFERME);

        PartecipazionePartita organizzatorePartecipazione = buildPartecipazione(
            organizzatore,
            OriginePartecipante.ORGANIZZATORE,
            true
        );
        partita.addPartecipante(organizzatorePartecipazione);

        LocalDateTime dataScadenzaInviti = LocalDateTime.now().plusHours(3);
        for (Long invitatoId : invitatiIds) {
            Utente invitato = getUtenteAttivo(invitatoId);

            PartecipazionePartita partecipazioneInvitato = buildPartecipazione(
                invitato,
                OriginePartecipante.INVITATO,
                true
            );
            partita.addPartecipante(partecipazioneInvitato);

            InvitoPartita invito = new InvitoPartita();
            invito.setMittente(organizzatore);
            invito.setDestinatario(invitato);
            invito.setStato(StatoInvitoPartita.IN_ATTESA);
            invito.setDataScadenza(dataScadenzaInviti);
            partita.addInvito(invito);
        }

        Partita salvata = partitaPadelRepository.save(partita);
        ricalcolaQuotePartita(salvata);
        salvata = partitaPadelRepository.save(salvata);

        adminNotificationService.notificaNuovaPrenotazione(salvata, organizzatore);
        return toPrenotazioneDTO(salvata);
    }

    private void validateRequestBase(PrenotazioneCreateRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Payload prenotazione mancante.");
        }
        if (request.getCampoId() == null || request.getDataOraInizio() == null || request.getDataOraFine() == null) {
            throw new IllegalArgumentException("campoId, dataOraInizio e dataOraFine sono obbligatori.");
        }
        if (!request.getDataOraFine().isAfter(request.getDataOraInizio())) {
            throw new IllegalArgumentException("La data/ora di fine deve essere successiva a quella di inizio.");
        }
    }

    private void validateFinestraPrenotazioneUser(LocalDateTime dataOraInizio) {
        LocalDateTime now = LocalDateTime.now();
        if (dataOraInizio.isBefore(now)) {
            throw new IllegalArgumentException("Non è possibile prenotare nel passato.");
        }

        YearMonth meseCorrente = YearMonth.from(now);
        YearMonth meseSuccessivo = meseCorrente.plusMonths(1);
        YearMonth meseTarget = YearMonth.from(dataOraInizio);

        if (!meseTarget.equals(meseCorrente) && !meseTarget.equals(meseSuccessivo)) {
            throw new IllegalArgumentException(
                "Gli utenti possono prenotare solo nel mese corrente o in quello successivo."
            );
        }
    }

    private List<Long> sanitizeInvitati(List<Long> invitatiIds, Long organizzatoreId) {
        if (invitatiIds == null || invitatiIds.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<Long> set = new LinkedHashSet<>();
        for (Long invitatoId : invitatiIds) {
            if (invitatoId == null) {
                continue;
            }
            if (invitatoId.equals(organizzatoreId)) {
                throw new IllegalArgumentException("L'organizzatore non può invitare se stesso.");
            }
            set.add(invitatoId);
        }

        if (set.size() > MAX_INVITI) {
            throw new IllegalArgumentException("Puoi invitare massimo 3 giocatori.");
        }

        return new ArrayList<>(set);
    }

    private PartecipazionePartita buildPartecipazione(Utente utente, OriginePartecipante origine, boolean confermato) {
        PartecipazionePartita partecipazione = new PartecipazionePartita();
        partecipazione.setUtente(utente);
        partecipazione.setOriginePartecipante(origine);
        partecipazione.setConfermato(confermato);
        partecipazione.setPagato(false);
        partecipazione.setQuotaDovuta(BigDecimal.ZERO);
        partecipazione.setQuotaPagata(BigDecimal.ZERO);
        partecipazione.setTotalePagato(BigDecimal.ZERO);
        return partecipazione;
    }

    private void aggiornaStatoPartitaDaInviti(Partita partita) {
        if (partita.getStato() == StatoPartita.CANCELLATA || partita.getStato() == StatoPartita.COMPLETATA) {
            return;
        }

        boolean hasPending = partita.getInviti() != null && partita.getInviti().stream()
            .anyMatch(invito -> invito.getStato() == StatoInvitoPartita.IN_ATTESA);

        partita.setStato(hasPending ? StatoPartita.IN_ATTESA_CONFERME : StatoPartita.CONFERMATA);
    }

    private void ricalcolaQuotePartita(Partita partita) {
        BigDecimal costoTotale = defaultIfNull(partita.getCostoTotale());
        long giocatoriConfermati = partita.getPartecipanti().stream()
            .filter(p -> Boolean.TRUE.equals(p.getConfermato()))
            .count();

        if (giocatoriConfermati <= 0) {
            partita.setQuotaPerGiocatore(BigDecimal.ZERO);
            for (PartecipazionePartita partecipazione : partita.getPartecipanti()) {
                if (!Boolean.TRUE.equals(partecipazione.getPagato())) {
                    partecipazione.setQuotaDovuta(BigDecimal.ZERO);
                }
                partecipazione.aggiornaStatoPagamento();
            }
            return;
        }

        BigDecimal quota = costoTotale.divide(BigDecimal.valueOf(giocatoriConfermati), 2, RoundingMode.HALF_UP);
        partita.setQuotaPerGiocatore(quota);

        for (PartecipazionePartita partecipazione : partita.getPartecipanti()) {
            if (Boolean.TRUE.equals(partecipazione.getConfermato())) {
                if (!Boolean.TRUE.equals(partecipazione.getPagato())) {
                    partecipazione.setQuotaDovuta(quota);
                }
            } else if (!Boolean.TRUE.equals(partecipazione.getPagato())) {
                partecipazione.setQuotaDovuta(BigDecimal.ZERO);
            }
            partecipazione.aggiornaStatoPagamento();
        }
    }

    private PrenotazioneDTO toPrenotazioneDTO(Partita partita) {
        PrenotazioneDTO dto = new PrenotazioneDTO();
        dto.setPartitaId(partita.getId());
        dto.setCampoId(partita.getCampo() != null ? partita.getCampo().getId() : null);
        dto.setCampoNome(partita.getCampo() != null ? partita.getCampo().getNome() : null);

        if (partita.getCreataDa() != null) {
            dto.setCreataDaUtenteId(partita.getCreataDa().getId());
            dto.setCreataDaNome(partita.getCreataDa().getNome());
            dto.setCreataDaCognome(partita.getCreataDa().getCognome());
        }

        dto.setDataOraInizio(partita.getDataOraInizio());
        dto.setDataOraFine(partita.getDataOraFine());
        dto.setStato(partita.getStato());
        dto.setCostoTotale(partita.getCostoTotale());
        dto.setQuotaPerGiocatore(partita.getQuotaPerGiocatore());
        dto.setNote(partita.getNote());

        List<InvitoPrenotazioneDTO> inviti = new ArrayList<>();
        if (partita.getInviti() != null) {
            partita.getInviti().stream()
                .sorted(Comparator.comparing(InvitoPartita::getDataScadenza, Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(invito -> {
                    InvitoPrenotazioneDTO invitoDTO = new InvitoPrenotazioneDTO();
                    invitoDTO.setInvitoId(invito.getId());
                    invitoDTO.setStato(invito.getStato());
                    invitoDTO.setDataScadenza(invito.getDataScadenza());
                    if (invito.getDestinatario() != null) {
                        invitoDTO.setDestinatarioId(invito.getDestinatario().getId());
                        invitoDTO.setDestinatarioNome(invito.getDestinatario().getNome());
                        invitoDTO.setDestinatarioCognome(invito.getDestinatario().getCognome());
                    }
                    inviti.add(invitoDTO);
                });
        }
        dto.setInviti(inviti);

        return dto;
    }

    private Partita getPartita(Long partitaId) {
        return partitaPadelRepository.findById(partitaId)
            .orElseThrow(() -> new IllegalArgumentException("Partita non trovata con id: " + partitaId));
    }

    private Utente getUtenteAttivo(Long utenteId) {
        Utente utente = utentePadelRepository.findById(utenteId)
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + utenteId));

        if (!Boolean.TRUE.equals(utente.getAttivo())) {
            throw new IllegalArgumentException("Utente non attivo: " + utenteId);
        }
        return utente;
    }

    private BigDecimal defaultIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private void requireAdminRole(String viewerRole) {
        if (!isAdminRole(viewerRole)) {
            throw new IllegalArgumentException("Operazione consentita solo a ROLE_ADMIN o ROLE_SUPERADMIN.");
        }
    }

    private boolean isAdminRole(String viewerRole) {
        if (viewerRole == null) {
            return false;
        }
        String role = viewerRole.trim().toUpperCase(Locale.ROOT);
        return role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPERADMIN");
    }

    private boolean isUserRole(String viewerRole) {
        if (viewerRole == null) {
            return false;
        }
        String role = viewerRole.trim().toUpperCase(Locale.ROOT);
        return role.equals("ROLE_USER");
    }
}
