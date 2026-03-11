package com.progetto.gestionale.dto.padel.cassa;

import java.util.ArrayList;
import java.util.List;

public class CassaTransazioneCreateRequestDTO {

    private Long clienteUtenteId;
    private Boolean clienteOccasionale;
    private String nominativoClienteOccasionale;
    private String metodoPagamento;
    private String tipo;
    private List<CassaRigaRequestDTO> righe = new ArrayList<>();

    public Long getClienteUtenteId() {
        return clienteUtenteId;
    }

    public void setClienteUtenteId(Long clienteUtenteId) {
        this.clienteUtenteId = clienteUtenteId;
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

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<CassaRigaRequestDTO> getRighe() {
        return righe;
    }

    public void setRighe(List<CassaRigaRequestDTO> righe) {
        this.righe = righe;
    }
}
