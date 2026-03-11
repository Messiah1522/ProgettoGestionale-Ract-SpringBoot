package com.progetto.gestionale.service;

import java.util.List;

import com.progetto.gestionale.entity.Prodotto;

public interface ProdottoService {

    Prodotto creaProdotto(Prodotto prodotto);

    List<Prodotto> trovaTuttiProdotti();

    Prodotto trovaProdottoPerId(Long id);

    Prodotto aggiornaProdotto(Long id, Prodotto dettagliProdotto);
    
}
