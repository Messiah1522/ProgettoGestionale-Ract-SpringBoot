package com.progetto.gestionale.dto.padel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartitaPagamentoDTO {

    private Long partitaId;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private BigDecimal costoTotale;
    private BigDecimal quotaPerGiocatore;
    private Integer numeroPartecipanti;
    private Integer numeroPaganti;
    private StatoPagamentoGenerale statoGenerale;
    private List<PartecipantePagamentoDTO> partecipanti = new ArrayList<>();

    public Long getPartitaId() {
        return partitaId;
    }

    public void setPartitaId(Long partitaId) {
        this.partitaId = partitaId;
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

    public Integer getNumeroPartecipanti() {
        return numeroPartecipanti;
    }

    public void setNumeroPartecipanti(Integer numeroPartecipanti) {
        this.numeroPartecipanti = numeroPartecipanti;
    }

    public Integer getNumeroPaganti() {
        return numeroPaganti;
    }

    public void setNumeroPaganti(Integer numeroPaganti) {
        this.numeroPaganti = numeroPaganti;
    }

    public StatoPagamentoGenerale getStatoGenerale() {
        return statoGenerale;
    }

    public void setStatoGenerale(StatoPagamentoGenerale statoGenerale) {
        this.statoGenerale = statoGenerale;
    }

    public List<PartecipantePagamentoDTO> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(List<PartecipantePagamentoDTO> partecipanti) {
        this.partecipanti = partecipanti;
    }
}
