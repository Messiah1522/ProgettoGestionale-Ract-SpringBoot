package com.progetto.gestionale.service.impl;
import com.progetto.gestionale.entity.Dipendente;
import com.progetto.gestionale.repository.DipendenteRepository;
import com.progetto.gestionale.service.DipendenteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class DipendenteServiceImpl implements DipendenteService {

    //repository usata per accedere al database
    private final DipendenteRepository dipendenteRepository;

    //iniezione del repository tramite costruttore.
    //spring crea automaticamente l'oggetto e lo passa qui
    public DipendenteServiceImpl(DipendenteRepository dipendenteRepository) {
        this.dipendenteRepository = dipendenteRepository;
    }

    @Transactional
    @Override
    public Dipendente creaDipendente(Dipendente dipendente) {
        /*
        regola di business: non possono esistere due dipendenti
        con la stessa email. Si usa Optional per verificare se
        esiste già
         */
        if(dipendenteRepository.findByEmail(dipendente.getEmail()).isPresent()){
            throw new IllegalArgumentException("email già esistente");
        }
        //salva il dipendente nel database
        return  dipendenteRepository.save(dipendente);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dipendente> trovaTutti() {
        return dipendenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Dipendente> trovaPerId(Long id) {
        return dipendenteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Dipendente> trovaPerEmail(String email) {
        return dipendenteRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dipendente> trovaPerRuolo(String ruolo) {
        return dipendenteRepository.findByRuolo(ruolo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dipendente> trovaConStipendioMaggioreDi(double stipendio) {
        return dipendenteRepository.findByStipendioGreaterThan(stipendio);
    }

    /*
        aggiorna un dipendente esistente, prima lo cerca
        per id e poi modifica i campi con i setter della
        entity 'Dipendente'
     */
    @Transactional
    @Override
    public Dipendente aggiornaDipendente(Long id, Dipendente dipendenteAggiornato) {
        Dipendente dipendente = dipendenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dipendente non trovato"));
        //si aggiornano i campi
        dipendente.setNome(dipendenteAggiornato.getNome());
        dipendente.setCognome(dipendenteAggiornato.getCognome());
        dipendente.setStipendio(dipendenteAggiornato.getStipendio());
        dipendente.setReparto(dipendenteAggiornato.getReparto());
        //si salva
        return dipendenteRepository.save(dipendente);
    }

    @Transactional
    @Override
    public void elimina(Long id) {
        if(!dipendenteRepository.existsById(id)){
            throw new IllegalArgumentException("dipendente non trovato");
        }
        dipendenteRepository.deleteById(id);
    }
}
