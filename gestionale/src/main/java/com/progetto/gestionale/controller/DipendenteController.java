package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.DipendenteDTO;
import com.progetto.gestionale.entity.Dipendente;
import com.progetto.gestionale.entity.Reparto;
import com.progetto.gestionale.response.ApiResponse;
import com.progetto.gestionale.service.DipendenteService;
import com.progetto.gestionale.service.RepartoService;
import com.progetto.gestionale.util.MapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    private final DipendenteService dipendenteService;
    private final RepartoService repartoService;

    public DipendenteController(DipendenteService dipendenteService, RepartoService repartoService) {
        this.dipendenteService = dipendenteService;
        this.repartoService = repartoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DipendenteDTO>> creaDipendente(@RequestBody DipendenteDTO dto){
        try{
            Dipendente entity = MapperUtils.toDipendenteEntity(dto);

            if(dto.getRepartoId() != null){
                Reparto reparto = repartoService.trovaPerId(dto.getRepartoId())
                        .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));
                entity.setReparto(reparto);
            }
            Dipendente saved = dipendenteService.creaDipendente(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Creato", MapperUtils.toDipendenteDTO(saved)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DipendenteDTO>>> trovaTutti(){
        List<DipendenteDTO> dtos = dipendenteService.trovaTutti()
                .stream()
                .map(MapperUtils::toDipendenteDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DipendenteDTO>> trovaPerId(@PathVariable Long id){
        return dipendenteService.trovaPerId(id)
                .map(d -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toDipendenteDTO(d))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Dipendente non trovato", null)));
    }

    @GetMapping("/search/by-email")
    public ResponseEntity<ApiResponse<DipendenteDTO>> trovaPerEmail(@RequestParam String email){
        return dipendenteService.trovaPerEmail(email)
                .map(d -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toDipendenteDTO(d))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Dipendente non trovato", null)));
    }

    @GetMapping("/search/by-ruolo")
    public ResponseEntity<ApiResponse<List<DipendenteDTO>>> trovaPerRuolo(@RequestParam String ruolo){
        List<DipendenteDTO> dtos = dipendenteService.trovaPerRuolo(ruolo)
                .stream()
                .map(MapperUtils::toDipendenteDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", dtos));
    }

    @GetMapping("/search/by-stipendio")
    public ResponseEntity<ApiResponse<List<DipendenteDTO>>> trovaPerStipendio(@RequestParam double min){
        List<DipendenteDTO> dtos = dipendenteService.trovaConStipendioMaggioreDi(min)
                .stream()
                .map(MapperUtils::toDipendenteDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", dtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DipendenteDTO>> aggiornaDipendente(@PathVariable Long id, @RequestBody DipendenteDTO dto){
        try{
            Dipendente entity = MapperUtils.toDipendenteEntity(dto);
            if(dto.getRepartoId() != null){
                Reparto reparto = repartoService.trovaPerId(dto.getRepartoId())
                        .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));
                entity.setReparto(reparto);
            }
            Dipendente updated = dipendenteService.aggiornaDipendente(id, entity);
            return ResponseEntity.ok(new ApiResponse<>(200, "Aggiornato", MapperUtils.toDipendenteDTO(updated)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> elimina(@PathVariable Long id){
        try{
            dipendenteService.elimina(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Eliminato", null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Dipendente non trovato", null));
        }
    }
}
