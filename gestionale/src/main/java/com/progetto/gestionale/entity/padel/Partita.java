package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.StatoPartita;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "partite",
    indexes = {
        @Index(name = "idx_partite_data_ora_inizio", columnList = "data_ora_inizio"),
        @Index(name = "idx_partite_stato", columnList = "stato")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_partite_campo_data_ora_inizio",
            columnNames = {"campo_id", "data_ora_inizio"}
        )
    }
)
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campo_id", nullable = false)
    private CampoPadel campo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creata_da_utente_id")
    private Utente creataDa;

    @Column(name = "data_ora_inizio", nullable = false)
    private LocalDateTime dataOraInizio;

    @Column(name = "data_ora_fine", nullable = false)
    private LocalDateTime dataOraFine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StatoPartita stato;

    @Column(name = "costo_totale", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoTotale;

    @Column(name = "quota_per_giocatore", precision = 10, scale = 2, nullable = false)
    private BigDecimal quotaPerGiocatore;

    @Column(length = 500)
    private String note;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartecipazionePartita> partecipanti = new ArrayList<>();

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvitoPartita> inviti = new ArrayList<>();

    public Partita() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (stato == null) {
            stato = StatoPartita.IN_ATTESA_CONFERME;
        }
        if (costoTotale == null) {
            costoTotale = BigDecimal.ZERO;
        }
        if (quotaPerGiocatore == null) {
            quotaPerGiocatore = BigDecimal.ZERO;
        }
        dataCreazione = now;
        dataUltimoAggiornamento = now;
        normalize();
    }

    @PreUpdate
    public void onUpdate() {
        dataUltimoAggiornamento = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (note != null) {
            note = note.trim();
        }
    }

    public void addPartecipante(PartecipazionePartita partecipazionePartita) {
        partecipanti.add(partecipazionePartita);
        partecipazionePartita.setPartita(this);
    }

    public void removePartecipante(PartecipazionePartita partecipazionePartita) {
        partecipanti.remove(partecipazionePartita);
        partecipazionePartita.setPartita(null);
    }

    public void addInvito(InvitoPartita invitoPartita) {
        inviti.add(invitoPartita);
        invitoPartita.setPartita(this);
    }

    public void removeInvito(InvitoPartita invitoPartita) {
        inviti.remove(invitoPartita);
        invitoPartita.setPartita(null);
    }

    public Long getId() {
        return id;
    }

    public CampoPadel getCampo() {
        return campo;
    }

    public void setCampo(CampoPadel campo) {
        this.campo = campo;
    }

    public Utente getCreataDa() {
        return creataDa;
    }

    public void setCreataDa(Utente creataDa) {
        this.creataDa = creataDa;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public StatoPartita getStato() {
        return stato;
    }

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public BigDecimal getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(BigDecimal costoTotale) {
        this.costoTotale = costoTotale;
    }

    public BigDecimal getQuotaPerGiocatore() {
        return quotaPerGiocatore;
    }

    public void setQuotaPerGiocatore(BigDecimal quotaPerGiocatore) {
        this.quotaPerGiocatore = quotaPerGiocatore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public List<PartecipazionePartita> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(List<PartecipazionePartita> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public List<InvitoPartita> getInviti() {
        return inviti;
    }

    public void setInviti(List<InvitoPartita> inviti) {
        this.inviti = inviti;
    }
}
