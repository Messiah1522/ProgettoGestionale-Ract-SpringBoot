package com.progetto.gestionale.service;
import com.progetto.gestionale.dto.ProgettoDTO;
import com.progetto.gestionale.entity.Progetto;

import java.util.List;
import java.util.Optional;

public interface ProgettoService {

    Progetto creaProgetto(ProgettoDTO progetto);

    List<Progetto> trovaTutti();

    Optional<Progetto> trovaPerId(long id);

    Optional<Progetto> trovaPerNome (String nome);

    Progetto aggiornaProgetto(Long id, Progetto progettoAggiornato);

    void elimina(Long id);

}
