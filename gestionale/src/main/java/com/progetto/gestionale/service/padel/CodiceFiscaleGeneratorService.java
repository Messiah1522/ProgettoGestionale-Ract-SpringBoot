package com.progetto.gestionale.service.padel;

import com.progetto.gestionale.entity.padel.ComuneItaliano;
import com.progetto.gestionale.repository.padel.ComuneItalianoRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CodiceFiscaleGeneratorService {

    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Map<Integer, Character> MAPPA_MESE = Map.ofEntries(
        Map.entry(1, 'A'),
        Map.entry(2, 'B'),
        Map.entry(3, 'C'),
        Map.entry(4, 'D'),
        Map.entry(5, 'E'),
        Map.entry(6, 'H'),
        Map.entry(7, 'L'),
        Map.entry(8, 'M'),
        Map.entry(9, 'P'),
        Map.entry(10, 'R'),
        Map.entry(11, 'S'),
        Map.entry(12, 'T')
    );

    private static final Map<Character, Integer> MAPPA_DISPARI = buildMappaDispari();
    private static final Map<Character, Integer> MAPPA_PARI = buildMappaPari();

    private final ComuneItalianoRepository comuneItalianoRepository;

    public CodiceFiscaleGeneratorService(ComuneItalianoRepository comuneItalianoRepository) {
        this.comuneItalianoRepository = comuneItalianoRepository;
    }

    public String calcolaCF(
        String nome,
        String cognome,
        LocalDate dataNascita,
        String sesso,
        String nomeComune
    ) {
        validateInput(nome, cognome, dataNascita, sesso, nomeComune);

        String codiceCognome = calcolaCodiceCognome(cognome);
        String codiceNome = calcolaCodiceNome(nome);
        String codiceAnno = String.format("%02d", dataNascita.getYear() % 100);
        char codiceMese = calcolaCodiceMese(dataNascita.getMonthValue());
        String codiceGiorno = calcolaCodiceGiornoESesso(dataNascita.getDayOfMonth(), sesso);
        String codiceComune = trovaCodiceComune(nomeComune);

        String parziale = codiceCognome + codiceNome + codiceAnno + codiceMese + codiceGiorno + codiceComune;
        char carattereControllo = calcolaCarattereControllo(parziale);

        return parziale + carattereControllo;
    }

    private void validateInput(
        String nome,
        String cognome,
        LocalDate dataNascita,
        String sesso,
        String nomeComune
    ) {
        if (isBlank(nome) || isBlank(cognome) || dataNascita == null || isBlank(sesso) || isBlank(nomeComune)) {
            throw new IllegalArgumentException("Dati insufficienti per il calcolo del Codice Fiscale.");
        }
    }

    private String calcolaCodiceCognome(String cognome) {
        String clean = normalizeLetters(cognome);
        return calcolaTreCaratteriStandard(clean);
    }

    private String calcolaCodiceNome(String nome) {
        String clean = normalizeLetters(nome);
        String consonanti = estraiConsonanti(clean);
        if (consonanti.length() >= 4) {
            return "" + consonanti.charAt(0) + consonanti.charAt(2) + consonanti.charAt(3);
        }
        return calcolaTreCaratteriStandard(clean);
    }

    private String calcolaTreCaratteriStandard(String testo) {
        String consonanti = estraiConsonanti(testo);
        String vocali = estraiVocali(testo);
        String base = consonanti + vocali + "XXX";
        return base.substring(0, 3);
    }

    private char calcolaCodiceMese(int mese) {
        Character lettera = MAPPA_MESE.get(mese);
        if (lettera == null) {
            throw new IllegalArgumentException("Mese non valido per il calcolo del Codice Fiscale.");
        }
        return lettera;
    }

    private String calcolaCodiceGiornoESesso(int giorno, String sesso) {
        int valoreGiorno;
        String normalizedSesso = sesso.trim().toUpperCase(Locale.ROOT);
        if (normalizedSesso.equals("F") || normalizedSesso.equals("FEMMINA") || normalizedSesso.equals("DONNA")) {
            valoreGiorno = giorno + 40;
        } else if (normalizedSesso.equals("M") || normalizedSesso.equals("MASCHIO") || normalizedSesso.equals("UOMO")) {
            valoreGiorno = giorno;
        } else {
            throw new IllegalArgumentException("Valore sesso non valido. Usa M/F oppure Maschio/Femmina.");
        }
        return String.format("%02d", valoreGiorno);
    }

    private String trovaCodiceComune(String nomeComune) {
        String comuneRicerca = nomeComune.trim();

        return comuneItalianoRepository.findFirstByNomeIgnoreCaseAndAttivoTrue(comuneRicerca)
            .map(ComuneItaliano::getCodiceCatastale)
            .orElseGet(() -> {
                List<ComuneItaliano> candidati =
                    comuneItalianoRepository.findByNomeContainingIgnoreCaseAndAttivoTrueOrderByNomeAsc(comuneRicerca);
                if (candidati.isEmpty()) {
                    throw new IllegalArgumentException("Comune non trovato: " + nomeComune);
                }
                return candidati.get(0).getCodiceCatastale();
            });
    }

    private char calcolaCarattereControllo(String codiceParziale16) {
        int somma = 0;
        for (int i = 0; i < codiceParziale16.length(); i++) {
            char carattere = codiceParziale16.charAt(i);
            if (i % 2 == 0) {
                Integer valore = MAPPA_DISPARI.get(carattere);
                if (valore == null) {
                    throw new IllegalArgumentException("Carattere non valido nel codice parziale.");
                }
                somma += valore;
            } else {
                Integer valore = MAPPA_PARI.get(carattere);
                if (valore == null) {
                    throw new IllegalArgumentException("Carattere non valido nel codice parziale.");
                }
                somma += valore;
            }
        }
        int resto = somma % 26;
        return ALFABETO.charAt(resto);
    }

    private String estraiConsonanti(String testo) {
        return testo.replaceAll("[AEIOU]", "");
    }

    private String estraiVocali(String testo) {
        return testo.replaceAll("[^AEIOU]", "");
    }

    private String normalizeLetters(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized
            .toUpperCase(Locale.ROOT)
            .replaceAll("[^A-Z]", "");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static Map<Character, Integer> buildMappaPari() {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i <= 9; i++) {
            map.put((char) ('0' + i), i);
        }
        for (int i = 0; i < 26; i++) {
            map.put((char) ('A' + i), i);
        }
        return map;
    }

    private static Map<Character, Integer> buildMappaDispari() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('0', 1);
        map.put('1', 0);
        map.put('2', 5);
        map.put('3', 7);
        map.put('4', 9);
        map.put('5', 13);
        map.put('6', 15);
        map.put('7', 17);
        map.put('8', 19);
        map.put('9', 21);
        map.put('A', 1);
        map.put('B', 0);
        map.put('C', 5);
        map.put('D', 7);
        map.put('E', 9);
        map.put('F', 13);
        map.put('G', 15);
        map.put('H', 17);
        map.put('I', 19);
        map.put('J', 21);
        map.put('K', 2);
        map.put('L', 4);
        map.put('M', 18);
        map.put('N', 20);
        map.put('O', 11);
        map.put('P', 3);
        map.put('Q', 6);
        map.put('R', 8);
        map.put('S', 12);
        map.put('T', 14);
        map.put('U', 16);
        map.put('V', 10);
        map.put('W', 22);
        map.put('X', 25);
        map.put('Y', 24);
        map.put('Z', 23);
        return map;
    }
}
