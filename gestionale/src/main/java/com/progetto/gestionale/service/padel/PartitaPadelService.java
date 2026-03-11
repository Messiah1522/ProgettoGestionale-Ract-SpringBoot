package com.progetto.gestionale.service.padel;

import com.progetto.gestionale.dto.padel.PartitaPagamentoDTO;

public interface PartitaPadelService {

    PartitaPagamentoDTO getPagamentiPartita(Long partitaId, Long viewerUserId, String viewerRole);

    PartitaPagamentoDTO ricalcolaQuote(Long partitaId, Long viewerUserId, String viewerRole);
}
