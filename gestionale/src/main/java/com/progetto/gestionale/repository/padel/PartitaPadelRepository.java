package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.Partita;
import com.progetto.gestionale.entity.padel.enums.StatoPartita;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartitaPadelRepository extends JpaRepository<Partita, Long> {

    @Override
    @EntityGraph(attributePaths = {
        "partecipanti",
        "partecipanti.utente",
        "partecipanti.pagamenti",
        "inviti",
        "inviti.destinatario",
        "campo",
        "creataDa"
    })
    Optional<Partita> findById(Long id);

    boolean existsByCampoIdAndDataOraInizioLessThanAndDataOraFineGreaterThanAndStatoNot(
        Long campoId,
        LocalDateTime dataOraFine,
        LocalDateTime dataOraInizio,
        StatoPartita statoEscluso
    );

    List<Partita> findByCampoIdAndDataOraInizioBetweenOrderByDataOraInizioAsc(
        Long campoId,
        LocalDateTime start,
        LocalDateTime end
    );

    List<Partita> findByDataOraInizioBetweenOrderByDataOraInizioAsc(
        LocalDateTime start,
        LocalDateTime end
    );
}
