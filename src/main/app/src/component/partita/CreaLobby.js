import React, { useState } from "react";
import { Button, Form, Container } from "react-bootstrap";

function CreaLobby() {
  const [difficolta, setDifficolta] = useState("");
  const [players, setPlayers] = useState("");

  const handleDifficolta = (e) => {
    setDifficolta(e.target.value);
  };

  // Funzione per generare le opzioni dei giocatori in base alla difficoltà
  const handlePlayersNumber = () => {
    switch (difficolta) {
      case "Easy":
        return [<option key="1" value="1">1</option>, <option key="2" value="2">2</option>, <option key="3" value="3">3</option>, <option key="3" value="3">4</option>];
      case "Medium":
        return [<option key="1" value="1">1</option>, <option key="2" value="2">2</option>, <option key="3" value="3">3</option>, <option key="4" value="4">4</option>, <option key="5" value="5">5</option>, <option key="6" value="6">6</option>];
      case "Hard":
        return [<option key="1" value="1">1</option>, <option key="2" value="2">2</option>, <option key="3" value="3">3</option>, <option key="4" value="4">4</option>, <option key="5" value="5">5</option>, <option key="6" value="6">6</option>, <option key="7" value="7">7</option>, <option key="8" value="8">8</option>];
      default:
        return [];
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Difficoltà: ", difficolta);
    console.log("Giocatori: ", players);
  };

  return (
    <div>
      <h1 className="display-3 text-center">Benvenuto in Crea Lobby</h1>
      <Form className="d-flex flex-column align-items-center justify-content-center" onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formOptionDifficolta">
          <Form.Label>A quale quiz vuoi rispondere?</Form.Label>
          <Form.Control as="select" value={difficolta} onChange={handleDifficolta}>
            <option value="">Seleziona un livello di difficoltà</option>
            <option value="Easy">Facile</option>
            <option value="Medium">Medio</option>
            <option value="Hard">Difficile</option>
          </Form.Control>
        </Form.Group>

        {difficolta && (
          <Form.Group className="mb-3" controlId="formOptionPlayers">
            <Form.Label>Numero Giocatori</Form.Label>
            <Form.Control as="select" value={players} onChange={(e) => setPlayers(e.target.value)}>
              <option value="">Seleziona il numero di giocatori</option>
              {handlePlayersNumber()}
            </Form.Control>
          </Form.Group>
        )}

        <Container className="d-flex justify-content-center">
          <Button className="text-center" type="submit">
            CREA
          </Button>
        </Container>
      </Form>
    </div>
  );
}

export default CreaLobby;
