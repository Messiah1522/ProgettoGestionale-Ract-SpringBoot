package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.Ruolo;
import com.progetto.gestionale.entity.padel.enums.NomeRuolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {

    Optional<Ruolo> findByNome(NomeRuolo nome);
}
