import React, { useState } from "react";
import { Button, Form, Container } from "react-bootstrap";

function CreaLobby({onLobbyCreated}) {
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

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Difficoltà: ", difficolta);
    console.log("Giocatori: ", players);
    console.log("nomeMappa: ", nomeMappa);

    fetch("/partita", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        difficolta: difficolta,
        players: players,
        nomeMappa: nomeMappa,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(
            `Creazione lobby fallita con stato: ${response.status}`
          );
        }
        console.log("response status:", response);
      })
      .then((data) => {
        console.log("Success:", data);
        setIsLobbyCreated("Lobby creata con successo!");
      })
      .catch((error) => {
        console.error("Error:", error);
        setIsLobbyCreated("Creazione lobby fallita. Per favore, riprova.");
      });
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
          <Button className="text-center" type="submit">
            CREA
          </Button>
        </Container>
      </Form>
      {isLobbyCreated && <p>{isLobbyCreated}</p>}
    </div>
  );
}

export default CreaLobby;
