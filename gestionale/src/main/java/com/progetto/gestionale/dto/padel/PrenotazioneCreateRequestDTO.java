package com.progetto.gestionale.dto.padel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneCreateRequestDTO {

    private Long campoId;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private BigDecimal costoTotale;
    private String note;
    private List<Long> invitatiIds = new ArrayList<>();

    public Long getCampoId() {
        return campoId;
    }

    public void setCampoId(Long campoId) {
        this.campoId = campoId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Long> getInvitatiIds() {
        return invitatiIds;
    }

    public void setInvitatiIds(List<Long> invitatiIds) {
        this.invitatiIds = invitatiIds;
    }
}
