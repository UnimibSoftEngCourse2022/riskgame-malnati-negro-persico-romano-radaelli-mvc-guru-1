import React, { useState } from "react";
import { Button, Form, Container } from "react-bootstrap";
import AppController from "../../application/AppController";

function CreaPartita() {
  const [difficolta, setDifficolta] = useState("");
  const [players, setPlayers] = useState("");
  const [nomeMappa, setNomeMappa] = useState("");
  const [isLobbyCreated, setIsLobbyCreated] = useState("");

  const handleDifficolta = (e) => {
    setDifficolta(e.target.value);
  };

  const handlePlayersNumber = () => {
    switch (difficolta) {
      case "EASY":
        return [
          <option key="2" value="2">
            2
          </option>,
          <option key="3" value="3">
            3
          </option>,
          <option key="4" value="4">
            4
          </option>,
        ];
      case "MEDIUM":
        return [
          <option key="2" value="2">
            2
          </option>,
          <option key="3" value="3">
            3
          </option>,
          <option key="4" value="4">
            4
          </option>,
          <option key="5" value="5">
            5
          </option>,
          <option key="6" value="6">
            6
          </option>,
        ];
      case "HARD":
        return [
          <option key="2" value="2">
            2
          </option>,
          <option key="3" value="3">
            3
          </option>,
          <option key="4" value="4">
            4
          </option>,
          <option key="5" value="5">
            5
          </option>,
          <option key="6" value="6">
            6
          </option>,
        ];
      default:
        return [];
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    //qui creo configurazione
    const configuration = {
    difficolta: difficolta,
    players: parseInt(players),
    nomeMappa: nomeMappa,
  };
    try {
      const result = await AppController.creaPartita(configuration);
      setIsLobbyCreated(
		  //qui non ti arrivava nessun id, ora ti arriva 
        `Lobby creata con successo! ID Lobby: ${result.id}`
      );
    } catch (error) {
      setIsLobbyCreated(`Errore nella creazione della lobby: ${error.message}`);
    }
  };

  return (
    <div>
      <h1 className="h3 text-center">Crea la tua lobby</h1>
      <Form
        className="d-flex flex-column align-items-center justify-content-center"
        onSubmit={handleSubmit}
      >
        <Form.Group className="mb-3" controlId="formOptionDifficolta">
          <Form.Label>A quale quiz vuoi rispondere?</Form.Label>
          <Form.Control
            as="select"
            value={difficolta}
            onChange={handleDifficolta}
          >
            <option value="">Seleziona un livello di difficoltà</option>
            <option value="EASY">Facile</option>
            <option value="MEDIUM">Medio</option>
            <option value="HARD">Difficile</option>
          </Form.Control>
        </Form.Group>

        {difficolta && (
          <Form.Group className="mb-3" controlId="formOptionPlayers">
            <Form.Label>Numero Giocatori</Form.Label>
            <Form.Control
              as="select"
              value={players}
              onChange={(e) => setPlayers(e.target.value)}
            >
              <option value="">Seleziona il numero di giocatori</option>
              {handlePlayersNumber()}
            </Form.Control>
          </Form.Group>
        )}

        <Form.Group className="mb-3">
          <Form.Label>Inserisci nome mappa</Form.Label>
          <Form.Control
            type="text"
            placeholder="mappa"
            value={nomeMappa}
            onChange={(e) => setNomeMappa(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formOptionDifficolta">
          <Form.Label>A quale quiz vuoi rispondere?</Form.Label>
        </Form.Group>

        <Container className="d-flex justify-content-center">
          <Button className="text-center text-uppercase" type="submit">
            Vai
          </Button>
        </Container>
      </Form>
      {isLobbyCreated && <p>{isLobbyCreated}</p>}
    </div>
  );
}

export default CreaPartita;
