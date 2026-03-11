package com.progetto.gestionale.entity.padel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "campi_padel",
    uniqueConstraints = @UniqueConstraint(name = "uk_campi_padel_nome", columnNames = "nome")
)
public class CampoPadel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 255)
    private String descrizione;

    @Column(nullable = false)
    private Boolean indoor;

    @Column(nullable = false)
    private Boolean attivo;

    @Column(name = "tariffa_oraria", precision = 10, scale = 2, nullable = false)
    private BigDecimal tariffaOraria;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    public CampoPadel() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (indoor == null) {
            indoor = false;
        }
        if (attivo == null) {
            attivo = true;
        }
        if (tariffaOraria == null) {
            tariffaOraria = BigDecimal.ZERO;
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
        if (nome != null) {
            nome = nome.trim();
        }
        if (descrizione != null) {
            descrizione = descrizione.trim();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getIndoor() {
        return indoor;
    }

    public void setIndoor(Boolean indoor) {
        this.indoor = indoor;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public BigDecimal getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(BigDecimal tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }
}
