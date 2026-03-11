package com.progetto.gestionale.service;
import com.progetto.gestionale.entity.Dipendente;

import java.util.List;
import java.util.Optional;

public interface DipendenteService {

    Dipendente creaDipendente(Dipendente dipendente);

    List<Dipendente> trovaTutti();

    Optional<Dipendente> trovaPerId(Long id);

    Optional<Dipendente> trovaPerEmail(String email);

    List<Dipendente> trovaPerRuolo(String ruolo);

    List<Dipendente> trovaConStipendioMaggioreDi(double stipendio);

    Dipendente aggiornaDipendente(Long id, Dipendente dipendenteAggiornato);

    void elimina(Long id);
}
