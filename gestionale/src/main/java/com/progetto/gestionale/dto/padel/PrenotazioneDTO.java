package com.progetto.gestionale.dto.padel;

import com.progetto.gestionale.entity.padel.enums.StatoPartita;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDTO {

    private Long partitaId;
    private Long campoId;
    private String campoNome;
    private Long creataDaUtenteId;
    private String creataDaNome;
    private String creataDaCognome;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private StatoPartita stato;
    private BigDecimal costoTotale;
    private BigDecimal quotaPerGiocatore;
    private String note;
    private List<InvitoPrenotazioneDTO> inviti = new ArrayList<>();

    public Long getPartitaId() {
        return partitaId;
    }

    public void setPartitaId(Long partitaId) {
        this.partitaId = partitaId;
    }

    public Long getCampoId() {
        return campoId;
    }

    public void setCampoId(Long campoId) {
        this.campoId = campoId;
    }

    public String getCampoNome() {
        return campoNome;
    }

    public void setCampoNome(String campoNome) {
        this.campoNome = campoNome;
    }

    public Long getCreataDaUtenteId() {
        return creataDaUtenteId;
    }

    public void setCreataDaUtenteId(Long creataDaUtenteId) {
        this.creataDaUtenteId = creataDaUtenteId;
    }

    public String getCreataDaNome() {
        return creataDaNome;
    }

    public void setCreataDaNome(String creataDaNome) {
        this.creataDaNome = creataDaNome;
    }

    public String getCreataDaCognome() {
        return creataDaCognome;
    }

    public void setCreataDaCognome(String creataDaCognome) {
        this.creataDaCognome = creataDaCognome;
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

    public List<InvitoPrenotazioneDTO> getInviti() {
        return inviti;
    }

    public void setInviti(List<InvitoPrenotazioneDTO> inviti) {
        this.inviti = inviti;
    }
}
