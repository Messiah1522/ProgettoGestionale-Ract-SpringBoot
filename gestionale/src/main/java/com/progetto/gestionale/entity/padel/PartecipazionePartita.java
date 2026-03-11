package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.OriginePartecipante;
import com.progetto.gestionale.entity.padel.enums.StatoPagamento;
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
    name = "partecipanti_partita",
    indexes = {
        @Index(name = "idx_partecipanti_partita_stato_pagamento", columnList = "stato_pagamento")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_partecipanti_partita_partita_utente",
            columnNames = {"partita_id", "utente_id"}
        )
    }
)
public class PartecipazionePartita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @Enumerated(EnumType.STRING)
    @Column(name = "origine_partecipante", nullable = false, length = 32)
    private OriginePartecipante originePartecipante;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato_pagamento", nullable = false, length = 16)
    private StatoPagamento statoPagamento;

    @Column(name = "quota_dovuta", precision = 10, scale = 2, nullable = false)
    private BigDecimal quotaDovuta;

    @Column(name = "quota_pagata", precision = 10, scale = 2, nullable = false)
    private BigDecimal quotaPagata;

    @Column(name = "totale_pagato", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalePagato;

    @Column(name = "metodo_pagamento", length = 32)
    private String metodoPagamento;

    @Column(nullable = false)
    private Boolean pagato;

    @Column(nullable = false)
    private Boolean confermato;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    @OneToMany(mappedBy = "partecipazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagamentoPartita> pagamenti = new ArrayList<>();

    public PartecipazionePartita() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (originePartecipante == null) {
            originePartecipante = OriginePartecipante.INVITATO;
        }
        if (statoPagamento == null) {
            statoPagamento = StatoPagamento.NON_PAGATO;
        }
        if (quotaDovuta == null) {
            quotaDovuta = BigDecimal.ZERO;
        }
        if (quotaPagata == null) {
            quotaPagata = BigDecimal.ZERO;
        }
        if (totalePagato == null) {
            totalePagato = BigDecimal.ZERO;
        }
        if (pagato == null) {
            pagato = false;
        }
        if (confermato == null) {
            confermato = false;
        }
        dataCreazione = now;
        dataUltimoAggiornamento = now;
        aggiornaStatoPagamento();
    }

    @PreUpdate
    public void onUpdate() {
        dataUltimoAggiornamento = LocalDateTime.now();
        aggiornaStatoPagamento();
    }

    public void addPagamento(PagamentoPartita pagamentoPartita) {
        pagamenti.add(pagamentoPartita);
        pagamentoPartita.setPartecipazione(this);
        if (pagamentoPartita.getImporto() != null) {
            totalePagato = totalePagato.add(pagamentoPartita.getImporto());
            quotaPagata = quotaPagata.add(pagamentoPartita.getImporto());
        }
        if (pagamentoPartita.getMetodoPagamento() != null) {
            metodoPagamento = pagamentoPartita.getMetodoPagamento().name();
        }
        aggiornaStatoPagamento();
    }

    public void aggiornaStatoPagamento() {
        if (totalePagato == null) {
            totalePagato = BigDecimal.ZERO;
        }
        if (quotaPagata == null) {
            quotaPagata = BigDecimal.ZERO;
        }
        if (quotaDovuta == null || quotaDovuta.compareTo(BigDecimal.ZERO) <= 0) {
            statoPagamento = StatoPagamento.NON_PAGATO;
            pagato = false;
            return;
        }
        if (totalePagato.compareTo(BigDecimal.ZERO) <= 0) {
            statoPagamento = StatoPagamento.NON_PAGATO;
            pagato = false;
        } else if (totalePagato.compareTo(quotaDovuta) >= 0) {
            statoPagamento = StatoPagamento.PAGATO;
            pagato = true;
        } else {
            statoPagamento = StatoPagamento.PARZIALE;
            pagato = false;
        }
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

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public OriginePartecipante getOriginePartecipante() {
        return originePartecipante;
    }

    public void setOriginePartecipante(OriginePartecipante originePartecipante) {
        this.originePartecipante = originePartecipante;
    }

    public StatoPagamento getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(StatoPagamento statoPagamento) {
        this.statoPagamento = statoPagamento;
    }

    public BigDecimal getQuotaDovuta() {
        return quotaDovuta;
    }

    public void setQuotaDovuta(BigDecimal quotaDovuta) {
        this.quotaDovuta = quotaDovuta;
    }

    public BigDecimal getQuotaPagata() {
        return quotaPagata;
    }

    public void setQuotaPagata(BigDecimal quotaPagata) {
        this.quotaPagata = quotaPagata;
    }

    public BigDecimal getTotalePagato() {
        return totalePagato;
    }

    public void setTotalePagato(BigDecimal totalePagato) {
        this.totalePagato = totalePagato;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Boolean getPagato() {
        return pagato;
    }

    public void setPagato(Boolean pagato) {
        this.pagato = pagato;
    }

    public Boolean getConfermato() {
        return confermato;
    }

    public void setConfermato(Boolean confermato) {
        this.confermato = confermato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public List<PagamentoPartita> getPagamenti() {
        return pagamenti;
    }

    public void setPagamenti(List<PagamentoPartita> pagamenti) {
        this.pagamenti = pagamenti;
    }
}
