package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.padel.UtenteLiteDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneResponseDTO;
import com.progetto.gestionale.service.padel.cassa.CassaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cassa")
public class CassaController {

    private final CassaService cassaService;

    public CassaController(CassaService cassaService) {
        this.cassaService = cassaService;
    }

    @PostMapping("/transazioni")
    public ResponseEntity<CassaTransazioneResponseDTO> registraTransazione(
        @RequestBody CassaTransazioneCreateRequestDTO request,
        @RequestParam(defaultValue = "ROLE_ADMIN") String viewerRole
    ) {
        CassaTransazioneResponseDTO dto = cassaService.registraTransazione(request, viewerRole);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/giocatori-presenti")
    public ResponseEntity<List<UtenteLiteDTO>> giocatoriPresenti(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime referenceTime
    ) {
        return ResponseEntity.ok(cassaService.getGiocatoriPresenti(referenceTime));
    }
}
