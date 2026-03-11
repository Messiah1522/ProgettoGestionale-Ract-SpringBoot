package com.progetto.gestionale.repository;
import com.progetto.gestionale.entity.Reparto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

//l'interfaccia si occupa dell'accesso ai dati della tabella reparti

public interface RepartoRepository extends JpaRepository<Reparto, Long> {

    Optional<Reparto> findByNome(String nome);

    boolean existsByNome (String nome);

}
