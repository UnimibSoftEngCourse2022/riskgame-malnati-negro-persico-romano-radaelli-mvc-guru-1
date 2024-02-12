import React from "react";
import { Container } from "react-bootstrap";
import ListGroup from "react-bootstrap/ListGroup";

function Istruzioni() {
  return (
    <Container className="my-4 p-4">
      <Container className="text-start shadow p-4 my-4">
        <h3 id="Istruzioni" className="h3">Istruzioni gioco Risiko</h3>
        <p>
          1. Scopo del gioco: Si parte sempre dall’obiettivo segreto scritto
          sulla carta che ogni giocatore riceverà ad inizio partita: la vittoria
          sarà del primo giocatore che raggiunge il suo obiettivo segreto.{" "}
          <br />
          2. Tabellone: Il tabellone di gioco è una mappa di 6 continenti divisi
          in 42 territori. Ogni continente è di un colore diverso e comprende da
          4 a 12 territori. <br />
          3. Armate: Ci sono 6 set completi di eserciti, ciascuno dei quali
          contiene 3 tipi di armata: Fanteria (valore 1), Cavalleria (del valore
          di 5 fanteria), Artiglieria (del valore di 10 Fanteria, o 2
          Cavalleria). <br />
          4. Impostazione della partita: Selezionare un colore e, a seconda del
          numero di giocatori, contare le “armate”. Partendo dal giocatore più
          giovane, vengono distribuite a ogni giocatore le carte territorio
          coperte. Si continua fino a quando tutti i 42 territori sono stati
          distribuiti. <br />
          5. Turno di gioco: Durante il proprio turno ogni giocatore può
          svolgere tre attività principali: rinforzare le proprie armate,
          attaccare, spostare. <br />
        </p>
      </Container>
      <Container className="text-start shadow p-4 my-4">
        <h3 id="difficolta"> Legenda Difficoltà</h3>
        <ListGroup as="ol" numbered>
          <ListGroup.Item as="li">
            Facile: la mappa si adatta riducendo i territori a 1/3, consentendo
            partite da 2 a 4 giocatori.{" "}
          </ListGroup.Item>
          <ListGroup.Item as="li">
            Media: la mappa si adatta a 2/3 dei territori originali, per partite
            da 2 a 6 giocatori.{" "}
          </ListGroup.Item>
          <ListGroup.Item as="li">
            Difficile: la mappa non subisce modifiche, consentendo partite
            complete da 2 a 6 giocatori.
          </ListGroup.Item>
        </ListGroup>
      </Container>
      <Container className="text-start shadow p-4 my-4">
        <h3 id="novita"> Novità del gioco</h3>
        <ListGroup as="ol">
          <ListGroup.Item as="li">
            Possibilità di creazione mappa personalizzata: solo l'utente loggato
            può accedere a questa funzione{" "}
          </ListGroup.Item>
          <ListGroup.Item as="li">
            Adattabilità mappa in base alla difficoltà
          </ListGroup.Item>
        </ListGroup>
      </Container>
    </Container>
  );
}

export default Istruzioni;
