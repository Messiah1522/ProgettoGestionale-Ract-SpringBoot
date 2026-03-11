package com.progetto.gestionale.service;
import com.progetto.gestionale.entity.Reparto;

import java.util.List;
import java.util.Optional;

public interface RepartoService {

    Reparto creaReparto(Reparto reparto);

    List<Reparto> trovaTutti();

    Optional<Reparto> trovaPerId(Long id);

    Optional<Reparto> trovaPerNome(String nome);

    Reparto aggiornaReparto (Long id, Reparto repartoAggiornato);

    void elimina(Long id);

}
