package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.ComuneItaliano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComuneItalianoRepository extends JpaRepository<ComuneItaliano, Long> {

    List<ComuneItaliano> findByNomeContainingIgnoreCaseAndAttivoTrueOrderByNomeAsc(String nome);

    Optional<ComuneItaliano> findFirstByNomeIgnoreCaseAndAttivoTrue(String nome);
}
