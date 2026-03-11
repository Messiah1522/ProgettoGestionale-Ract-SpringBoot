package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.padel.AggiungiGiocatoreAdminRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneAdminCreateRequestDTO;
import com.progetto.gestionale.dto.padel.PrenotazioneDTO;
import com.progetto.gestionale.service.padel.PrenotazioneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/prenotazioni")
public class AdminPrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public AdminPrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping("/per-conto-di")
    public ResponseEntity<PrenotazioneDTO> creaPerContoDi(
        @RequestBody PrenotazioneAdminCreateRequestDTO request,
        @RequestParam(defaultValue = "ROLE_ADMIN") String viewerRole
    ) {
        PrenotazioneDTO dto = prenotazioneService.creaPrenotazionePerContoDiAdmin(request, viewerRole);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/{partitaId}/aggiungi-giocatore")
    public ResponseEntity<PrenotazioneDTO> aggiungiGiocatore(
        @PathVariable Long partitaId,
        @RequestBody AggiungiGiocatoreAdminRequestDTO request,
        @RequestParam(defaultValue = "ROLE_ADMIN") String viewerRole
    ) {
        PrenotazioneDTO dto = prenotazioneService.aggiungiGiocatoreAdmin(partitaId, request.getUtenteId(), viewerRole);
        return ResponseEntity.ok(dto);
    }
}
