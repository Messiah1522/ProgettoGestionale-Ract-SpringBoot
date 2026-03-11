package com.progetto.gestionale.dto.padel;

public class PrenotazioneAdminCreateRequestDTO extends PrenotazioneCreateRequestDTO {

    private Long utenteId;

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }
}
