package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.padel.PartitaPagamentoDTO;
import com.progetto.gestionale.service.padel.PartitaPadelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partite")
public class PartitaPadelController {

    private final PartitaPadelService partitaPadelService;

    public PartitaPadelController(PartitaPadelService partitaPadelService) {
        this.partitaPadelService = partitaPadelService;
    }

    @GetMapping("/{id}/pagamenti")
    public ResponseEntity<PartitaPagamentoDTO> getPagamentiPartita(
        @PathVariable Long id,
        @RequestParam(required = false) Long viewerUserId,
        @RequestParam(defaultValue = "ROLE_USER") String viewerRole
    ) {
        PartitaPagamentoDTO dto = partitaPadelService.getPagamentiPartita(id, viewerUserId, viewerRole);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/ricalcola-quote")
    public ResponseEntity<PartitaPagamentoDTO> ricalcolaQuote(
        @PathVariable Long id,
        @RequestParam(required = false) Long viewerUserId,
        @RequestParam(defaultValue = "ROLE_ADMIN") String viewerRole
    ) {
        PartitaPagamentoDTO dto = partitaPadelService.ricalcolaQuote(id, viewerUserId, viewerRole);
        return ResponseEntity.ok(dto);
    }
}
