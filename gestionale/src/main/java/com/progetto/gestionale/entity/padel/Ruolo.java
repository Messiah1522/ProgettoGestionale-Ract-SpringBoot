package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.NomeRuolo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "ruoli",
    uniqueConstraints = @UniqueConstraint(name = "uk_ruoli_nome", columnNames = "nome")
)
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private NomeRuolo nome;

    @ManyToMany(mappedBy = "ruoli")
    private Set<Utente> utenti = new HashSet<>();

    public Ruolo() {
    }

    public Ruolo(NomeRuolo nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public NomeRuolo getNome() {
        return nome;
    }

    public void setNome(NomeRuolo nome) {
        this.nome = nome;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }
}
