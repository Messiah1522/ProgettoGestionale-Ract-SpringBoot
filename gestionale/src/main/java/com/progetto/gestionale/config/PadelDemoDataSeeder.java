package com.progetto.gestionale.config;

import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.entity.padel.CampoPadel;
import com.progetto.gestionale.entity.padel.Ruolo;
import com.progetto.gestionale.entity.padel.Utente;
import com.progetto.gestionale.entity.padel.enums.NomeRuolo;
import com.progetto.gestionale.entity.padel.enums.Sesso;
import com.progetto.gestionale.repository.ProdottoRepository;
import com.progetto.gestionale.repository.padel.CampoPadelRepository;
import com.progetto.gestionale.repository.padel.RuoloRepository;
import com.progetto.gestionale.repository.padel.UtentePadelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class PadelDemoDataSeeder {

    @Bean
    CommandLineRunner seedCampi(CampoPadelRepository campoPadelRepository) {
        return args -> {
            if (campoPadelRepository.count() > 0) {
                return;
            }

            CampoPadel campo1 = new CampoPadel();
            campo1.setNome("Campo 1");
            campo1.setDescrizione("Campo panoramico");
            campo1.setIndoor(false);
            campo1.setAttivo(true);
            campo1.setTariffaOraria(new BigDecimal("40.00"));

            CampoPadel campo2 = new CampoPadel();
            campo2.setNome("Campo 2");
            campo2.setDescrizione("Campo coperto");
            campo2.setIndoor(true);
            campo2.setAttivo(true);
            campo2.setTariffaOraria(new BigDecimal("42.00"));

            campoPadelRepository.save(campo1);
            campoPadelRepository.save(campo2);
        };
    }

    @Bean
    CommandLineRunner seedRuoliUtentiDemo(
        RuoloRepository ruoloRepository,
        UtentePadelRepository utentePadelRepository
    ) {
        return args -> {
            Ruolo roleUser = ensureRole(ruoloRepository, NomeRuolo.ROLE_USER);
            Ruolo roleAdmin = ensureRole(ruoloRepository, NomeRuolo.ROLE_ADMIN);
            Ruolo roleSuperAdmin = ensureRole(ruoloRepository, NomeRuolo.ROLE_SUPERADMIN);

            upsertDemoUser(
                utentePadelRepository,
                "Luca",
                "Verdi",
                "user@appiapadel.local",
                "User123!",
                "VRDLCU90A01L049M",
                Sesso.M,
                LocalDate.of(1990, 1, 1),
                roleUser
            );

            upsertDemoUser(
                utentePadelRepository,
                "Marta",
                "Rossi",
                "admin@appiapadel.local",
                "Admin123!",
                "RSSMRT85B15H501P",
                Sesso.F,
                LocalDate.of(1985, 2, 15),
                roleAdmin
            );

            upsertDemoUser(
                utentePadelRepository,
                "Andrea",
                "Bianchi",
                "superadmin@appiapadel.local",
                "Super123!",
                "BNCNDR80C20F205Q",
                Sesso.M,
                LocalDate.of(1980, 3, 20),
                roleSuperAdmin
            );
        };
    }

    @Bean
    CommandLineRunner seedProdottiDemo(ProdottoRepository prodottoRepository) {
        return args -> {
            if (prodottoRepository.count() > 0) {
                return;
            }

            prodottoRepository.save(createProdotto("Acqua 50cl", 1.50, 120L));
            prodottoRepository.save(createProdotto("Palline Padel", 7.50, 48L));
            prodottoRepository.save(createProdotto("Integratore", 3.00, 35L));
            prodottoRepository.save(createProdotto("Grip Racchetta", 5.50, 20L));
        };
    }

    private Ruolo ensureRole(RuoloRepository ruoloRepository, NomeRuolo nomeRuolo) {
        return ruoloRepository.findByNome(nomeRuolo).orElseGet(() -> ruoloRepository.save(new Ruolo(nomeRuolo)));
    }

    private void upsertDemoUser(
        UtentePadelRepository utentePadelRepository,
        String nome,
        String cognome,
        String email,
        String password,
        String codiceFiscale,
        Sesso sesso,
        LocalDate dataNascita,
        Ruolo ruolo
    ) {
        Utente utente = utentePadelRepository.findFirstByEmailIgnoreCase(email)
            .orElseGet(() -> utentePadelRepository.findFirstByCodiceFiscaleIgnoreCase(codiceFiscale).orElseGet(Utente::new));

        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPasswordHash(password);
        utente.setCodiceFiscale(codiceFiscale);
        utente.setSesso(sesso);
        utente.setDataNascita(dataNascita);
        utente.setAttivo(true);
        utente.setTelefono("0000000000");
        utente.setRuoli(Set.of(ruolo));

        utentePadelRepository.save(utente);
    }

    private Prodotto createProdotto(String titolo, double prezzo, long quantita) {
        Prodotto prodotto = new Prodotto();
        prodotto.setTitolo(titolo);
        prodotto.setDescrizione("Prodotto demo");
        prodotto.setPrezzo(prezzo);
        prodotto.setDisponibile(quantita > 0);
        prodotto.setQuantitaMagazzino(quantita);
        prodotto.setUrlImmagine(null);
        return prodotto;
    }
}
