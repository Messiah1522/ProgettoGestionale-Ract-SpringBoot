# Kickstarter per gestionale con Spring Boot e React
Questo è un codice kickstarter per l'esercitazione della classe React e Java Spring Boot sullo sviluppo di un gestionale.

## Informazioni sul progetto
Breve panoramica sul progetto e sulle tecnologie utilizzate

- Sviluppatore: Michele Mincone
- Data: 27 febbraio 2026
- Tecnologie:
    - Frontend:
        - Vite
        - React
        - Altre dipendenze quali:
            - TailwindCSS per lo stile
            - React Router per il routing frontend
            - Axios per le chiamate HTTP
    - Backend:
        - Java
        - Spring Boot
- Requisiti e installazioni sul tuo computer: 
    - Java 
    - Node.js 
    - Visual Studio Code

# Struttura del progetto
All'interno di questa repository troverai due cartelle:

- frontend -> dove scriverai il codice per il tuo frontend in React e Javascript
- gestionale -> il backend in cui si trova Spring Boot

# Come iniziare

Prima di avviare il progetto assicurati di avere Java installato sul tuo computer. Scegli la versione 25, 21 o quella che preferisci.

Poi assicurati di avere le seguenti estensioni sul tuo IDE VS CODE:

- Language Support for Java (TM) by Red Hat
- Extension Pack for Java (Microsoft)
- Spring Boot Dashboard (Microsoft)
- Spring Boot Extension Pack (VMWare)
- Spring Boot Snippets (Developer Soapbox)

Inoltre, assicurati di avere Node.js installato sul tuo computer.

Segui i prossimi passi fondamentali per una corretta esecuzione del progetto.

## Clona questa repository

Clona questa repository sul tuo desktop con:

`git clone https://github.com/mikeisadev/gestionale-react-spring.git`

Altrimenti, se hai già una cartella col nome del progetto, ad esempio "gestionale-react-spring" e vuoi le cartelle di backend e frontend direttamente li dentro esegui il comando aggiungendo un punto "." alla fine del comando:

`git clone https://github.com/mikeisadev/gestionale-react-spring.git .` 

## Verificare la versione di Java

Per iniziare è fondamentale verificare la versione di Java.

Prima verifichiamo quale versione di Java hai sul tuo computer con il comando:

`java --version`

Se ti esce `25.0.1` memorizza il numero "25", mentre se ti esce `24.0.3` memorizza "24"

## Modifica la versione di Java sul backend se necessario

Apri la cartella "gestionale" e apri il file "pom.xml". 

Verifica cosa c'è scritto dentro i tag "properties" e successivamente dentro "java.version":

```xml
<properties>
		<java.version>25</java.version>
</properties>
```

Io ad esempio ho la versione "25".

Quindi, in base alla versione che hai inserisci il numero corrispondente senza punti, deve essere un numero intero.

## Driver del database

Apri il file "application.properties" sito in questo percorso:

`gestionale/src/main/resources/application.properties`

Ora dovrai modificare alcune voci. 

Se hai MySQL installato sul tuo computer userai questo driver:

`spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`

Dovrai quindi commentare l'altro driver.

Invece, se hai MariaDB commenta l'altro driver di MySQL e decommenta questo driver:

`spring.datasource.driver-class-name=org.mariadb.jdbc.Driver`

## Connessione al database

Sempre all'interno di "application.properties" nel percorso:

`gestionale/src/main/resources/application.properties`

Dovrai fare attenzione alla stringa di connessione:

`spring.datasource.url=jdbc:mariadb://127.0.0.1:3306/gestionale_industriatech`

Qui analizziamo pezzo per pezzo la stringa.

Se hai MariaDB lasci il frammento "jdbc:mariadb", altrimenti se hai MySQL inserirai "jdbc:mysql".

La parte finale della stringa "/gestionale_industriatech" è il nome del database che dovrai
creare tu stesso a mano.

Esegui questo step perché è fondamentale.

Se non creerai il database riceverai un errore, perché Spring Boot non saprà a quale database connettersi.

NOTA 1: fai anche attenzione alla porta. Normalmente è impostata su 3306 con il frammento di stringa 127.0.0.1:3306. Questo perché la porta di default dei database MySQL/MariaDB sono su quella porta. Se non dovesse partire la connessione al database, verifica anche la porta del tuo servizio di database MySQL/MariaDB e eventualmente cambiala.

NOTA 2: verifica sempre di aver avviato il tuo database. Altrimenti non sarà disponibile il servizio stesso del database.

## Installare le dipendenze del Frontend

Ora dovrai installare le dipendenze del frontend prima di poterlo avviare.

Ricordati che il frontend è stato avviato per te grazie al tool Vite assieme a React e altre dipendenze quali: React Router per il routing frontend, Axios per le chiamate HTTP e Tailwind per lo stile.

Quindi hai già tutto quanto messo a disposizione.

La prima cosa da fare è entrare nella cartelle frontend con questo comando:

`cd frontend`

Una volta dentro la cartella, esegui questo comando per installare tutte le dipendenze con il comando npm di Node.js:

`npm install` oppure `npm i` (comando abbreviato)

Attendi l'installazione delle dipendenze.

Una volta finita l'installazione puoi avviare il server Vite per il frontend con il seguente comando:

`npm run dev`

Visita l'indirizzo che il server Vite genera per te e visualizza il frontend.

# Avviare il backend

A questo punto puoi avviare il tuo backend direttamente su Visual Studio Code usando il tool "Spring Boot Dashboard" che hai precedentemente installato come estensione.

Dovrai ricercerare l'icona di Spring Boot (si tratta di un'icona simile a un pulsante di accensione) e successivamente dovrai cliccare sull'icona "start" o "play" per avviare il backend.

Se non ti appare quell'icona, prova a usare la barra di ricerca di Visual Studio Code premendo la combo di pulsanti:

`CTRL + P` (WINDOWS)
`CMD + p` (MacOS)

A questo punto cerca questa stringa:

`> Spring Boot Dashboard`

Dovrai cliccare sulla voce in elenco tra i risultati di ricerca:

`Spring Boot Dashboard: Run`

Quindi clicca quella voce e attendi l'avvio del backend che girerà su un porta separata dal frontend.

NOTA: se non appare la voce di Spring Boot Dashboard verifica se hai scritto correttamente. Altrimenti è probabile che non hai le estensioni per Visual Studio Code.

NOTA: verifica se vengono generati degli errori all'avvio di Spring Boot e cerca di correggerli seguendo gli step scritti all'inizio di questo file ReadMe, perché potrebbe essere che non hai configurato correttamente l'ambiente. 

