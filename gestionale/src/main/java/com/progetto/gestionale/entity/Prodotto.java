package com.progetto.gestionale.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="prodotti")
public class Prodotto {

    /**
     * DEFINIAMO LE PROPRIETà
     * 
     * Le proprietà definiscono le colonne della tabella "prodotti"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;

    private String descrizione;

    private String urlImmagine;

    private Double prezzo;

    private Boolean disponibile;

    private Long quantitaMagazzino;

    private LocalDateTime dataCreazione;

    private LocalDateTime dataUltimoAggiornamento;
    
    @PrePersist
    public void onCreate(){
        dataCreazione = LocalDateTime.now();
        dataUltimoAggiornamento = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        dataUltimoAggiornamento = LocalDateTime.now();
    }

    /**
     * DEFINIAMO IL COSTRUTTORE VUOTO E PIENO
     * 
     * JPA utilizza il costruttore vuoto per inizializzare la classe e creare
     * la tabella che corrisponde a questa classe
     * 
     * Il costruttore pieno lo usiamo noi
     */
    public Prodotto() {
    }

    public Prodotto(
        String titolo, 
        String descrizione, 
        String urlImmagine, 
        Double prezzo, 
        Boolean disponibile,
        Long quantitaMagazzino, 
        LocalDateTime dataCreazione, 
        LocalDateTime dataUltimoAggiornamento
    ) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
        this.prezzo = prezzo;
        this.disponibile = disponibile;
        this.quantitaMagazzino = quantitaMagazzino;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    /**
     * GENERIAMO TUTTI I GETTERS & SETTERS
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrlImmagine() {
        return urlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Boolean getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(Boolean disponibile) {
        this.disponibile = disponibile;
    }

    public Long getQuantitaMagazzino() {
        return quantitaMagazzino;
    }

    public void setQuantitaMagazzino(Long quantitaMagazzino) {
        this.quantitaMagazzino = quantitaMagazzino;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(LocalDateTime dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }
    
}
