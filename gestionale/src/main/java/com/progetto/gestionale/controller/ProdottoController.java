package com.progetto.gestionale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.service.ProdottoService;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoService prodottoService;
    
    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    /**
     * Questo è il metodo che mi da tutti i prodotti
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Prodotto>> tuttiProdotti() {
        List<Prodotto> tuttiProdotti = prodottoService.trovaTuttiProdotti();

        return ResponseEntity.ok(tuttiProdotti);
    }

    /**
     * Questo è il metodo per creare tutti i prodotti
     */
    @PostMapping
    public ResponseEntity<Prodotto> creaProdotto(@RequestBody Prodotto prodotto) {
        Prodotto nuovoProdotto = prodottoService.creaProdotto(prodotto);

        return new ResponseEntity<>(nuovoProdotto, HttpStatus.CREATED);
    }
}
