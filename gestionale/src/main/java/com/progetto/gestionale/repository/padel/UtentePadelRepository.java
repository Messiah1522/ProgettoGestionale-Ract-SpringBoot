package com.progetto.gestionale.repository.padel;

import com.progetto.gestionale.entity.padel.Utente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtentePadelRepository extends JpaRepository<Utente, Long> {

    @Query("""
        SELECT u
        FROM Utente u
        WHERE u.attivo = true
          AND (
            LOWER(u.nome) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(u.cognome) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(CONCAT(u.nome, ' ', u.cognome)) LIKE LOWER(CONCAT('%', :term, '%'))
          )
        ORDER BY u.cognome ASC, u.nome ASC
        """)
    List<Utente> searchAttivi(@Param("term") String term, Pageable pageable);

    @Query("""
        SELECT DISTINCT u
        FROM Utente u
        LEFT JOIN FETCH u.ruoli r
        WHERE u.attivo = true
          AND (
            LOWER(u.email) = LOWER(:identifier)
            OR UPPER(u.codiceFiscale) = UPPER(:identifier)
          )
        """)
    Optional<Utente> findAttivoByIdentifierWithRuoli(@Param("identifier") String identifier);

    Optional<Utente> findFirstByEmailIgnoreCaseAndAttivoTrue(String email);

    Optional<Utente> findFirstByCodiceFiscaleIgnoreCaseAndAttivoTrue(String codiceFiscale);

    Optional<Utente> findFirstByEmailIgnoreCase(String email);

    Optional<Utente> findFirstByCodiceFiscaleIgnoreCase(String codiceFiscale);
}
