package com.progetto.gestionale.entity.padel;

import com.progetto.gestionale.entity.padel.enums.Sesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "utenti",
    indexes = {
        @Index(name = "idx_utenti_cognome_nome", columnList = "cognome,nome")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_utenti_codice_fiscale", columnNames = "codice_fiscale"),
        @UniqueConstraint(name = "uk_utenti_email", columnNames = "email")
    }
)
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String cognome;

    @Email
    @NotBlank
    @Column(nullable = false, length = 180)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotBlank
    @Column(name = "codice_fiscale", nullable = false, length = 16)
    private String codiceFiscale;

    @Column(name = "data_nascita")
    private LocalDate dataNascita;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Sesso sesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comune_nascita_id")
    private ComuneItaliano comuneNascita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comune_residenza_id")
    private ComuneItaliano comuneResidenza;

    @Column(length = 30)
    private String telefono;

    @Column(nullable = false)
    private Boolean attivo;

    @Column(name = "punti_fedelta", nullable = false)
    private Long puntiFedelta;

    @Column(name = "data_registrazione", nullable = false)
    private LocalDateTime dataRegistrazione;

    @Column(name = "data_ultimo_aggiornamento", nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "utenti_ruoli",
        joinColumns = @JoinColumn(name = "utente_id"),
        inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private Set<Ruolo> ruoli = new HashSet<>();

    public Utente() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (attivo == null) {
            attivo = true;
        }
        if (puntiFedelta == null) {
            puntiFedelta = 0L;
        }
        dataRegistrazione = now;
        dataUltimoAggiornamento = now;
        normalize();
    }

    @PreUpdate
    public void onUpdate() {
        dataUltimoAggiornamento = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (nome != null) {
            nome = nome.trim();
        }
        if (cognome != null) {
            cognome = cognome.trim();
        }
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        if (codiceFiscale != null) {
            codiceFiscale = codiceFiscale.trim().toUpperCase();
        }
        if (telefono != null) {
            telefono = telefono.trim();
        }
    }

    public void addRuolo(Ruolo ruolo) {
        ruoli.add(ruolo);
        ruolo.getUtenti().add(this);
    }

    public void removeRuolo(Ruolo ruolo) {
        ruoli.remove(ruolo);
        ruolo.getUtenti().remove(this);
    }

    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Sesso getSesso() {
        return sesso;
    }

    public void setSesso(Sesso sesso) {
        this.sesso = sesso;
    }

    public ComuneItaliano getComuneNascita() {
        return comuneNascita;
    }

    public void setComuneNascita(ComuneItaliano comuneNascita) {
        this.comuneNascita = comuneNascita;
    }

    public ComuneItaliano getComuneResidenza() {
        return comuneResidenza;
    }

    public void setComuneResidenza(ComuneItaliano comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public Long getPuntiFedelta() {
        return puntiFedelta;
    }

    public void setPuntiFedelta(Long puntiFedelta) {
        this.puntiFedelta = puntiFedelta;
    }

    public LocalDateTime getDataRegistrazione() {
        return dataRegistrazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public Set<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(Set<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }
}
