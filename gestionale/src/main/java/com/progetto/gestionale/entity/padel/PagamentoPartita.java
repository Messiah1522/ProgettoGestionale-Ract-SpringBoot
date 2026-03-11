package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.MetodoPagamento;
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
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamenti_partita")
public class PagamentoPartita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partecipazione_id", nullable = false)
    private PartecipazionePartita partecipazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrato_da_utente_id")
    private Utente registratoDa;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal importo;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 16)
    private MetodoPagamento metodoPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column(length = 300)
    private String note;

    public PagamentoPartita() {
    }

    @PrePersist
    public void onCreate() {
        if (metodoPagamento == null) {
            metodoPagamento = MetodoPagamento.CONTANTI;
        }
        if (dataPagamento == null) {
            dataPagamento = LocalDateTime.now();
        }
        if (note != null) {
            note = note.trim();
        }
    }

    public Long getId() {
        return id;
    }

    public PartecipazionePartita getPartecipazione() {
        return partecipazione;
    }

    public void setPartecipazione(PartecipazionePartita partecipazione) {
        this.partecipazione = partecipazione;
    }

    public Utente getRegistratoDa() {
        return registratoDa;
    }

    public void setRegistratoDa(Utente registratoDa) {
        this.registratoDa = registratoDa;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
