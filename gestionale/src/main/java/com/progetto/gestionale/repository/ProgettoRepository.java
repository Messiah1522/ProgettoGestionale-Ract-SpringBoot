package com.progetto.gestionale.repository;
import com.progetto.gestionale.entity.Progetto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgettoRepository extends JpaRepository<Progetto, Long> {

    List<Progetto> findByBudgetGreaterThan(double budget);

    List<Progetto> findByBudgetLessThan(double Budget);

    Optional<Progetto> findByNome(String nome);
}
