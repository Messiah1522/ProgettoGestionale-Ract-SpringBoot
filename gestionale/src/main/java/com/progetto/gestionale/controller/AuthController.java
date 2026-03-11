package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.padel.auth.LoginRequestDTO;
import com.progetto.gestionale.dto.padel.auth.LoginResponseDTO;
import com.progetto.gestionale.entity.padel.Utente;
import com.progetto.gestionale.entity.padel.enums.NomeRuolo;
import com.progetto.gestionale.repository.padel.UtentePadelRepository;
import com.progetto.gestionale.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtentePadelRepository utentePadelRepository;

    public AuthController(UtentePadelRepository utentePadelRepository) {
        this.utentePadelRepository = utentePadelRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        if (request == null || isBlank(request.getIdentifier()) || isBlank(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, "Identifier e password sono obbligatori.", null));
        }

        String identifier = request.getIdentifier().trim();
        Utente utente = utentePadelRepository.findAttivoByIdentifierWithRuoli(identifier).orElse(null);

        if (utente == null || !Objects.equals(utente.getPasswordHash(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, "Credenziali non valide.", null));
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(utente.getId());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setEmail(utente.getEmail());
        response.setRole(resolvePrimaryRole(utente).name());
        response.setToken("demo-jwt-" + utente.getId() + "-" + System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    private NomeRuolo resolvePrimaryRole(Utente utente) {
        boolean hasSuperAdmin = utente.getRuoli().stream()
            .anyMatch(ruolo -> ruolo.getNome() == NomeRuolo.ROLE_SUPERADMIN);
        if (hasSuperAdmin) {
            return NomeRuolo.ROLE_SUPERADMIN;
        }

        boolean hasAdmin = utente.getRuoli().stream()
            .anyMatch(ruolo -> ruolo.getNome() == NomeRuolo.ROLE_ADMIN);
        if (hasAdmin) {
            return NomeRuolo.ROLE_ADMIN;
        }

        return NomeRuolo.ROLE_USER;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
