package com.progetto.gestionale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
//// @Entity dice a JPA/Hibernate che questa classe rappresenta una tabella del database
@Table(name= "reparti")
//serve a èer dare un nome esplicito alla tabella, best practice
public class Reparto {
    @Id
    // indica che questo campo è la chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // il database genera automaticamente l'id (auto_increment)
    private Long id;

    @NotBlank
    // il nome del reparto non può essere nullo o vuoto
    @Column(unique = true)
    //impongo l'unicità, cio non possono esiste due reparti con lo stesso nome
    private String nome;

    private String descrizione;
    // non metto column perché non ho proprietà da dichiarare

    @OneToMany(mappedBy = "reparto")
    /*Relazione uno a molti, cioè un reparto può avere molti
    dipendenti. La foreign key è nella tabella dipendenti
     */
    private List<Dipendente> dipendenti;

    private LocalDateTime dataCreazione;

    private LocalDateTime dataUltimoAggiornamento;

    //getter e setter perché servono a Hibernate e vengono usati dal service

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<Dipendente> getDipendenti() {
        return dipendenti;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDipendenti(List<Dipendente> dipendenti) {
        this.dipendenti = dipendenti;
    }

    @PrePersist
    /* metodo eseguito automaticamente prima che l'entità
        venga salvata nel database
     */
    public void onCreate(){
        dataCreazione = LocalDateTime.now();
        dataUltimoAggiornamento = LocalDateTime.now();
    }

    @PreUpdate
    /*
        metodo eseguito automaticamente prima che l'enttià
        venga aggiornata nel database
     */
    public void onUpdate(){
        dataUltimoAggiornamento = LocalDateTime.now();
    }
}
