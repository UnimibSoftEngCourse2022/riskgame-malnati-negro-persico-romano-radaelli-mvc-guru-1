import React from 'react';
import { Container } from 'react-bootstrap';

function Istruzioni() {
  return (
	<Container className="">
      <h2 className= "text-center display-3" > Istruzioni</h2>
      <p>
        1. Scopo del gioco: Si parte sempre dall’obiettivo segreto scritto sulla carta che ogni giocatore riceverà ad inizio partita: la vittoria sarà del primo giocatore che raggiunge il suo obiettivo segreto.<br />
        2. Tabellone: Il tabellone di gioco è una mappa di 6 continenti divisi in 42 territori. Ogni continente è di un colore diverso e comprende da 4 a 12 territori.<br />
        3. Armate: Ci sono 6 set completi di eserciti, ciascuno dei quali contiene 3 tipi di armata: Fanteria (valore 1), Cavalleria (del valore di 5 fanteria), Artiglieria (del valore di 10 Fanteria, o 2 Cavalleria).<br />
        4. Impostazione della partita: Selezionare un colore e, a seconda del numero di giocatori, contare le “armate”. Partendo dal giocatore più giovane, vengono distribuite a ogni giocatore le carte territorio coperte. Si continua fino a quando tutti i 42 territori sono stati distribuiti.<br />
        5. Turno di gioco: Durante il proprio turno ogni giocatore può svolgere tre attività principali: rinforzare le proprie armate, attaccare, spostare.<br />
      </p>
    </Container>
  );
}

export default Istruzioni;
