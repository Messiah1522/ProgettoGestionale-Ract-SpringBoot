package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.PartecipazionePartita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartecipazionePartitaRepository extends JpaRepository<PartecipazionePartita, Long> {

    List<PartecipazionePartita> findByPartitaId(Long partitaId);

    Optional<PartecipazionePartita> findByPartitaIdAndUtenteId(Long partitaId, Long utenteId);
}
