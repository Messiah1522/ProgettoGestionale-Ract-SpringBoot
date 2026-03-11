package com.progetto.gestionale.dto.padel;

import java.math.BigDecimal;

public class PartecipantePagamentoDTO {

    private Long partecipazioneId;
    private Long utenteId;
    private String nome;
    private String cognome;
    private BigDecimal quotaDaPagare;
    private BigDecimal quotaPagata;
    private boolean hasPagato;
    private String metodoPagamento;

    public Long getPartecipazioneId() {
        return partecipazioneId;
    }

    public void setPartecipazioneId(Long partecipazioneId) {
        this.partecipazioneId = partecipazioneId;
    }

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public BigDecimal getQuotaDaPagare() {
        return quotaDaPagare;
    }

    public void setQuotaDaPagare(BigDecimal quotaDaPagare) {
        this.quotaDaPagare = quotaDaPagare;
    }

    public BigDecimal getQuotaPagata() {
        return quotaPagata;
    }

    public void setQuotaPagata(BigDecimal quotaPagata) {
        this.quotaPagata = quotaPagata;
    }

    public boolean isHasPagato() {
        return hasPagato;
    }

    public void setHasPagato(boolean hasPagato) {
        this.hasPagato = hasPagato;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
