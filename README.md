# Risk Game

![Version](https://img.shields.io/badge/version-4.0.0-success)
![License](https://img.shields.io/github/license/UnimibSoftEngCourse2022/riskgame-malnati-negro-persico-romano-radaelli-mvc-guru-1)
![Stars](https://img.shields.io/github/stars/UnimibSoftEngCourse2022/riskgame-malnati-negro-persico-romano-radaelli-mvc-guru-1)

<img src="images\RiskGame.png" alt="RISK" width="100%">

## Esecuzione progetto

### Prerequisiti

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html) (3.1 or >)
- [Git](https://git-scm.com/downloads)

### Import e installazione 

- `git clone https://github.com/UnimibSoftEngCourse2022/riskgame-malnati-negro-persico-romano-radaelli-mvc-guru-1.git`: per eseguire la clone del repository localmente.

*I seguenti comandi sono da eseguire all'interno della root del repository*

- `mvn package`: Esegue la build del server e i relativi test. 
Se i test hanno successo, esegue anche la build del client all'interno di `src/main/app` e copia i file generati
in `target/static`, dove `target/` è la cartella in cui risiedono i file compilati del server.
Infine, crea il pacchetto .jar completo.

- `java -jar target/com.mvcguru.risiko.maven.eclipse-0.0.1-SNAPSHOT.jar`: Esegue il file .jar generato in precedenza.

- Il server è ora accessibile all'indirizzo `localhost:8080`.
<img src="images\homePage.png" alt="HomePage" width="100%">


## Come giocare
*Per giocare devi conoscere le regole di Risiko*  

Per rinfrescarti la memoria nella pagina principale c'è una sintesi delle regole di gioco prese dal gioco ufficiale  
Dopo aver ripassato, avrai due possibilità:

- Registrarti, loggarti
- Entrare come ospite  

In entrambi i casi potrai passare alla pagina successiva cliccando su cliccando 'Play Game',  
dopodichè ti troverai nella pagina di creazione della lobby nella quale dovrai:

- Creare una lobby, quindi scegliere difficoltà, numero giocatori e darle un nome
- Entrare in una lobby esistente  

*La partita comincia in automatico quando la Lobby è piena*  
#### N.B : nelle due modalità media e facile l'unione dei territori della modalità difficile si nota dai confini grigi (rispetto al nero classico)  
Qua sotto un immagine per invogliarti a provare il nostro gioco... Buon divertimento! 

<img src="images\Mappa_6players.png" alt="Mappa da 6 persone" width="100%">

*made by MVC-GURU*
