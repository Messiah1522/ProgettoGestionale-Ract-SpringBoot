package com.progetto.gestionale.repository;
import com.progetto.gestionale.entity.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {

    Optional<Dipendente> findByEmail(String email);

    List<Dipendente> findByRuolo(String ruolo);

    List<Dipendente> findByStipendioGreaterThan(double stipendio);

}
