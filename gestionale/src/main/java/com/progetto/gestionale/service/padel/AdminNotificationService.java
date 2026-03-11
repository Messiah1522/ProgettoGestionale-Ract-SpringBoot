package com.progetto.gestionale.service.padel;

import com.progetto.gestionale.entity.padel.Partita;
import com.progetto.gestionale.entity.padel.Utente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminNotificationService {

    private static final Logger log = LoggerFactory.getLogger(AdminNotificationService.class);

    @Value("${app.padel.admin-notification-email:admin@appiapadel.local}")
    private String adminNotificationEmail;

    public void notificaNuovaPrenotazione(Partita partita, Utente organizzatore) {
        log.info(
            "[NOTIFICA_ADMIN] Nuova prenotazione per {} - organizzatore: {} {} (id={}) campo={} inizio={} fine={} partitaId={}",
            adminNotificationEmail,
            organizzatore.getNome(),
            organizzatore.getCognome(),
            organizzatore.getId(),
            partita.getCampo() != null ? partita.getCampo().getNome() : "N/D",
            partita.getDataOraInizio(),
            partita.getDataOraFine(),
            partita.getId()
        );
    }
}
