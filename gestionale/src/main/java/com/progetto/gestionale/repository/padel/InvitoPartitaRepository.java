package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.InvitoPartita;
import com.progetto.gestionale.entity.padel.enums.StatoInvitoPartita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvitoPartitaRepository extends JpaRepository<InvitoPartita, Long> {

    List<InvitoPartita> findByStatoAndDataScadenzaBefore(StatoInvitoPartita stato, LocalDateTime dataLimite);

    Optional<InvitoPartita> findByIdAndDestinatarioId(Long id, Long destinatarioId);

    List<InvitoPartita> findByPartitaId(Long partitaId);
}
