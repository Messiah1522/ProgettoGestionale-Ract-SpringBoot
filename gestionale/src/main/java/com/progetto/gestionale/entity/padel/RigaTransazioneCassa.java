package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.Prodotto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "righe_transazioni_cassa")
public class RigaTransazioneCassa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transazione_id", nullable = false)
    private TransazioneCassa transazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    @Column(nullable = false, length = 180)
    private String descrizione;

    @Column(nullable = false)
    private Long quantita;

    @Column(name = "prezzo_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal prezzoUnitario;

    @Column(name = "totale_riga", precision = 10, scale = 2, nullable = false)
    private BigDecimal totaleRiga;

    public RigaTransazioneCassa() {
    }

    @PrePersist
    public void onCreate() {
        if (quantita == null || quantita < 0) {
            quantita = 0L;
        }
        if (prezzoUnitario == null) {
            prezzoUnitario = BigDecimal.ZERO;
        }
        normalize();
        calcolaTotaleRiga();
    }

    @PreUpdate
    public void onUpdate() {
        normalize();
        calcolaTotaleRiga();
    }

    private void normalize() {
        if (descrizione != null) {
            descrizione = descrizione.trim();
        }
    }

    private void calcolaTotaleRiga() {
        totaleRiga = prezzoUnitario.multiply(BigDecimal.valueOf(quantita));
    }

    public Long getId() {
        return id;
    }

    public TransazioneCassa getTransazione() {
        return transazione;
    }

    public void setTransazione(TransazioneCassa transazione) {
        this.transazione = transazione;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getQuantita() {
        return quantita;
    }

    public void setQuantita(Long quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public BigDecimal getTotaleRiga() {
        return totaleRiga;
    }

    public void setTotaleRiga(BigDecimal totaleRiga) {
        this.totaleRiga = totaleRiga;
    }
}
