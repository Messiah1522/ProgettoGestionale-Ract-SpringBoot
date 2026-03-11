package com.progetto.gestionale.controller;

import com.progetto.gestionale.dto.ProgettoDTO;
import com.progetto.gestionale.entity.Progetto;
import com.progetto.gestionale.response.ApiResponse;
import com.progetto.gestionale.service.ProgettoService;
import com.progetto.gestionale.util.MapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progetti")
public class ProgettoController {

    private final ProgettoService progettoService;

    public ProgettoController(ProgettoService progettoService) {
        this.progettoService = progettoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProgettoDTO>> creaProgetto(@RequestBody ProgettoDTO dto){
        try{
            Progetto saved = progettoService.creaProgetto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Creato", MapperUtils.toProgettoDTO(saved)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgettoDTO>>> trovaTutti(){
        List<ProgettoDTO> dtos = progettoService.trovaTutti()
                .stream()
                .map(MapperUtils::toProgettoDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgettoDTO>> trovaPerId(@PathVariable long id){
        return progettoService.trovaPerId(id)
                .map(p -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toProgettoDTO(p))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Progetto non trovato", null)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProgettoDTO>> trovaPerNome(@RequestParam String nome){
        return progettoService.trovaPerNome(nome)
                .map(p -> ResponseEntity.ok(new ApiResponse<>(200, "OK", MapperUtils.toProgettoDTO(p))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Progetto non trovato", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgettoDTO>> aggiornaProgetto(@PathVariable Long id, @RequestBody ProgettoDTO dto){
        try{
            Progetto aggiornamento = MapperUtils.toProgettoEntity(dto);
            Progetto updated = progettoService.aggiornaProgetto(id, aggiornamento);
            return ResponseEntity.ok(new ApiResponse<>(200, "Aggiornato", MapperUtils.toProgettoDTO(updated)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> elimina(@PathVariable Long id){
        try{
            progettoService.elimina(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Eliminato", null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Progetto non trovato", null));
        }
    }
}
