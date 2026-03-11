package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.StatoInvitoPartita;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "inviti_partita",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_inviti_partita_partita_destinatario",
            columnNames = {"partita_id", "destinatario_id"}
        )
    }
)
public class InvitoPartita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mittente_id", nullable = false)
    private Utente mittente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Utente destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StatoInvitoPartita stato;

    @Column(name = "data_invio", nullable = false)
    private LocalDateTime dataInvio;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDateTime dataScadenza;

    @Column(name = "data_risposta")
    private LocalDateTime dataRisposta;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    public InvitoPartita() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (stato == null) {
            stato = StatoInvitoPartita.IN_ATTESA;
        }
        if (dataInvio == null) {
            dataInvio = now;
        }
        if (dataScadenza == null) {
            dataScadenza = now.plusHours(3);
        }
        dataUltimoAggiornamento = now;
    }

    @PreUpdate
    public void onUpdate() {
        dataUltimoAggiornamento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Partita getPartita() {
        return partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public Utente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Utente destinatario) {
        this.destinatario = destinatario;
    }

    public StatoInvitoPartita getStato() {
        return stato;
    }

    public void setStato(StatoInvitoPartita stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    public LocalDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public LocalDateTime getDataRisposta() {
        return dataRisposta;
    }

    public void setDataRisposta(LocalDateTime dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }
}
