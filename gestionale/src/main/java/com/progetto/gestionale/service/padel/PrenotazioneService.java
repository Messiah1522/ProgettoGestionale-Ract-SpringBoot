package com.progetto.gestionale.service.padel;

import com.progetto.gestionale.dto.padel.PrenotazioneAdminCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneDTO;
import com.progetto.gestionale.dto.padel.PartitaCalendarioDTO;
import com.progetto.gestionale.dto.padel.SlotOccupatoDTO;
import com.progetto.gestionale.dto.padel.UtenteLiteDTO;

import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneService {

    PrenotazioneDTO creaPrenotazioneUtente(PrenotazioneCreateRequestDTO request, Long viewerUserId, String viewerRole);

    PrenotazioneDTO creaPrenotazionePerContoDiAdmin(PrenotazioneAdminCreateRequestDTO request, String viewerRole);

    PrenotazioneDTO aggiungiGiocatoreAdmin(Long partitaId, Long utenteId, String viewerRole);

    PrenotazioneDTO rispondiInvito(Long invitoId, Long destinatarioId, boolean accetta);

    List<SlotOccupatoDTO> getSlotOccupati(Long campoId, LocalDate giorno);

    List<PartitaCalendarioDTO> getPartiteGiorno(LocalDate giorno, Long campoId, Long viewerUserId, String viewerRole);

    List<UtenteLiteDTO> cercaGiocatori(String term, Long excludeUserId, Integer limit);

    void processaInvitiScaduti();
}
