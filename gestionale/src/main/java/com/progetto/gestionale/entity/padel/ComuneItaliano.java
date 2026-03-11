package com.progetto.gestionale.entity.padel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "comuni_italiani",
    indexes = {
        @Index(name = "idx_comuni_italiani_nome", columnList = "nome"),
        @Index(name = "idx_comuni_italiani_provincia", columnList = "provincia_sigla")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_comuni_italiani_codice_catastale", columnNames = "codice_catastale")
    }
)
public class ComuneItaliano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String nome;

    @Column(name = "provincia_sigla", nullable = false, length = 2)
    private String provinciaSigla;

    @Column(name = "codice_catastale", nullable = false, length = 4)
    private String codiceCatastale;

    @Column(length = 5)
    private String cap;

    @Column(nullable = false)
    private Boolean attivo;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    public ComuneItaliano() {
    }

    public ComuneItaliano(String nome, String provinciaSigla, String codiceCatastale, String cap) {
        this.nome = nome;
        this.provinciaSigla = provinciaSigla;
        this.codiceCatastale = codiceCatastale;
        this.cap = cap;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (attivo == null) {
            attivo = true;
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
        if (provinciaSigla != null) {
            provinciaSigla = provinciaSigla.trim().toUpperCase();
        }
        if (codiceCatastale != null) {
            codiceCatastale = codiceCatastale.trim().toUpperCase();
        }
        if (nome != null) {
            nome = nome.trim();
        }
        if (cap != null) {
            cap = cap.trim();
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

    public String getProvinciaSigla() {
        return provinciaSigla;
    }

    public void setProvinciaSigla(String provinciaSigla) {
        this.provinciaSigla = provinciaSigla;
    }

    public String getCodiceCatastale() {
        return codiceCatastale;
    }

    public void setCodiceCatastale(String codiceCatastale) {
        this.codiceCatastale = codiceCatastale;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }
}
