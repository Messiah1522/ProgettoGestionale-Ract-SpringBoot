package com.progetto.gestionale.controller;

import com.progetto.gestionale.entity.padel.CampoPadel;
import com.progetto.gestionale.repository.padel.CampoPadelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campi")
public class CampoPadelController {

    private final CampoPadelRepository campoPadelRepository;

    public CampoPadelController(CampoPadelRepository campoPadelRepository) {
        this.campoPadelRepository = campoPadelRepository;
    }

    @GetMapping
    public ResponseEntity<List<CampoPadel>> listaCampi() {
        return ResponseEntity.ok(campoPadelRepository.findAll());
    }
}
