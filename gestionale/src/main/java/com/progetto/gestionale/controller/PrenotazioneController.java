package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.padel.PrenotazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneDTO;
import com.progetto.gestionale.dto.padel.PartitaCalendarioDTO;
import com.progetto.gestionale.dto.padel.RispostaInvitoDTO;
import com.progetto.gestionale.dto.padel.SlotOccupatoDTO;
import com.progetto.gestionale.dto.padel.UtenteLiteDTO;
import com.progetto.gestionale.service.padel.PrenotazioneService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping
    public ResponseEntity<PrenotazioneDTO> creaPrenotazione(
        @RequestBody PrenotazioneCreateRequestDTO request,
        @RequestParam Long viewerUserId,
        @RequestParam(defaultValue = "ROLE_USER") String viewerRole
    ) {
        PrenotazioneDTO dto = prenotazioneService.creaPrenotazioneUtente(request, viewerUserId, viewerRole);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/inviti/{invitoId}/risposta")
    public ResponseEntity<PrenotazioneDTO> rispondiInvito(
        @PathVariable Long invitoId,
        @RequestParam Long viewerUserId,
        @RequestBody RispostaInvitoDTO request
    ) {
        boolean accetta = request != null && Boolean.TRUE.equals(request.getAccetta());
        PrenotazioneDTO dto = prenotazioneService.rispondiInvito(invitoId, viewerUserId, accetta);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/slot-occupati")
    public ResponseEntity<List<SlotOccupatoDTO>> getSlotOccupati(
        @RequestParam Long campoId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate giorno
    ) {
        return ResponseEntity.ok(prenotazioneService.getSlotOccupati(campoId, giorno));
    }

    @GetMapping("/giorno")
    public ResponseEntity<List<PartitaCalendarioDTO>> getPartiteGiorno(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate giorno,
        @RequestParam(required = false) Long campoId,
        @RequestParam(required = false) Long viewerUserId,
        @RequestParam(defaultValue = "ROLE_USER") String viewerRole
    ) {
        return ResponseEntity.ok(
            prenotazioneService.getPartiteGiorno(giorno, campoId, viewerUserId, viewerRole)
        );
    }

    @GetMapping("/amici")
    public ResponseEntity<List<UtenteLiteDTO>> cercaAmici(
        @RequestParam String q,
        @RequestParam(required = false) Long excludeUserId,
        @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(prenotazioneService.cercaGiocatori(q, excludeUserId, limit));
    }
}
