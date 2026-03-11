package com.progetto.gestionale.dto.padel.cassa;

public class CassaRigaRequestDTO {

    private Long prodottoId;
    private Long quantita;

    public Long getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Long prodottoId) {
        this.prodottoId = prodottoId;
    }

    public Long getQuantita() {
        return quantita;
    }

    public void setQuantita(Long quantita) {
        this.quantita = quantita;
    }
}
