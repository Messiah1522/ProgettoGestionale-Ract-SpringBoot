package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.RepartoDTO;
import com.progetto.gestionale.entity.Reparto;
import com.progetto.gestionale.response.ApiResponse;
import com.progetto.gestionale.service.RepartoService;
import com.progetto.gestionale.util.MapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reparti")
public class RepartoController {

    private final RepartoService repartoService;

    public RepartoController(RepartoService repartoService) {
        this.repartoService = repartoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RepartoDTO>> creaReparto(@RequestBody RepartoDTO dto){
        try{
            Reparto saved = repartoService.creaReparto(MapperUtils.toRepartoEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Reparto creato", MapperUtils.toRepartoDTO(saved)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RepartoDTO>>> trovaTutti(){
        List<RepartoDTO> dtos = repartoService.trovaTutti()
                .stream()
                .map(MapperUtils::toRepartoDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RepartoDTO>> trovaPerId(@PathVariable Long id){
        return repartoService.trovaPerId(id)
                .map(r -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toRepartoDTO(r))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Reparto non trovato", null)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<RepartoDTO>> trovaPerNome(@RequestParam String nome){
        return repartoService.trovaPerNome(nome)
                .map(r -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toRepartoDTO(r))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Reparto non trovato", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RepartoDTO>> aggiornaReparto(@PathVariable Long id, @RequestBody RepartoDTO dto){
        try{
            Reparto updated = repartoService.aggiornaReparto(id, MapperUtils.toRepartoEntity(dto));
            return ResponseEntity.ok(new ApiResponse<>(200, "Aggiornato", MapperUtils.toRepartoDTO(updated)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> elimina(@PathVariable Long id){
        try{
            repartoService.elimina(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Eliminato", null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Reparto non trovato", null));
        }
    }
}
