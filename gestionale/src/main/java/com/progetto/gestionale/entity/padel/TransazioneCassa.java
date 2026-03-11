package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.MetodoPagamento;
import com.progetto.gestionale.entity.padel.enums.TipoTransazioneCassa;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "transazioni_cassa",
    indexes = {
        @Index(name = "idx_transazioni_cassa_data", columnList = "data_transazione"),
        @Index(name = "idx_transazioni_cassa_tipo", columnList = "tipo")
    }
)
public class TransazioneCassa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TipoTransazioneCassa tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 16)
    private MetodoPagamento metodoPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_pagante_id")
    private Utente clientePagante;

    @Column(name = "cliente_occasionale", nullable = false)
    private Boolean clienteOccasionale;

    @Column(name = "nominativo_cliente_occasionale", length = 160)
    private String nominativoClienteOccasionale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id")
    private Partita partita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrata_da_utente_id")
    private Utente registrataDa;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totale;

    @Column(name = "data_transazione", nullable = false)
    private LocalDateTime dataTransazione;

    @Column(length = 300)
    private String note;

    @OneToMany(mappedBy = "transazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RigaTransazioneCassa> righe = new ArrayList<>();

    public TransazioneCassa() {
    }

    @PrePersist
    public void onCreate() {
        if (tipo == null) {
            tipo = TipoTransazioneCassa.VENDITA_MAGAZZINO;
        }
        if (metodoPagamento == null) {
            metodoPagamento = MetodoPagamento.CONTANTI;
        }
        if (clienteOccasionale == null) {
            clienteOccasionale = false;
        }
        if (totale == null) {
            totale = BigDecimal.ZERO;
        }
        if (dataTransazione == null) {
            dataTransazione = LocalDateTime.now();
        }
        normalize();
    }

    @PreUpdate
    public void onUpdate() {
        normalize();
    }

    private void normalize() {
        if (nominativoClienteOccasionale != null) {
            nominativoClienteOccasionale = nominativoClienteOccasionale.trim();
        }
        if (note != null) {
            note = note.trim();
        }
    }

    public void addRiga(RigaTransazioneCassa riga) {
        righe.add(riga);
        riga.setTransazione(this);
        ricalcolaTotale();
    }

    public void removeRiga(RigaTransazioneCassa riga) {
        righe.remove(riga);
        riga.setTransazione(null);
        ricalcolaTotale();
    }

    public void ricalcolaTotale() {
        BigDecimal nuovoTotale = BigDecimal.ZERO;
        for (RigaTransazioneCassa riga : righe) {
            if (riga.getTotaleRiga() != null) {
                nuovoTotale = nuovoTotale.add(riga.getTotaleRiga());
            }
        }
        totale = nuovoTotale;
    }

    public Long getId() {
        return id;
    }

    public TipoTransazioneCassa getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransazioneCassa tipo) {
        this.tipo = tipo;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Utente getClientePagante() {
        return clientePagante;
    }

    public void setClientePagante(Utente clientePagante) {
        this.clientePagante = clientePagante;
    }

    public Boolean getClienteOccasionale() {
        return clienteOccasionale;
    }

    public void setClienteOccasionale(Boolean clienteOccasionale) {
        this.clienteOccasionale = clienteOccasionale;
    }

    public String getNominativoClienteOccasionale() {
        return nominativoClienteOccasionale;
    }

    public void setNominativoClienteOccasionale(String nominativoClienteOccasionale) {
        this.nominativoClienteOccasionale = nominativoClienteOccasionale;
    }

    public Partita getPartita() {
        return partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }

    public Utente getRegistrataDa() {
        return registrataDa;
    }

    public void setRegistrataDa(Utente registrataDa) {
        this.registrataDa = registrataDa;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public LocalDateTime getDataTransazione() {
        return dataTransazione;
    }

    public void setDataTransazione(LocalDateTime dataTransazione) {
        this.dataTransazione = dataTransazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<RigaTransazioneCassa> getRighe() {
        return righe;
    }

    public void setRighe(List<RigaTransazioneCassa> righe) {
        this.righe = righe;
    }
}
