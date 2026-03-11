package com.progetto.gestionale.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.repository.ProdottoRepository;
import com.progetto.gestionale.service.ProdottoService;


@Service
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository prodottoRepository;

    public ProdottoServiceImpl(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    @Transactional
    public Prodotto creaProdotto(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    @Override
    @Transactional
    public List<Prodotto> trovaTuttiProdotti() {
        return prodottoRepository.findAll();
    }

    @Override
    @Transactional
    public Prodotto trovaProdottoPerId(Long id) {
        return prodottoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));
    }

    @Override
    @Transactional
    public Prodotto aggiornaProdotto(Long id, Prodotto dettagliProdotto) {
        Prodotto prodottoEsistente = trovaProdottoPerId(id);

        prodottoEsistente.setTitolo(dettagliProdotto.getTitolo());

        return prodottoRepository.save(prodottoEsistente);
    }
    
}
