import React, { useState } from "react";
import { Alert, Button, Container } from "react-bootstrap";
import appController from "../../application/AppController";

function ConsoleDifesa({ idPartita, difendente }) {
  const [numeroDadi, setNumeroDadi] = useState(0);

  const handleDice = () => {
    setNumeroDadi(numeroDadi + 1);

    console.log("numeroDadi", numeroDadi);
  };
  const handleDefense = () => {
    const oggettoDifesa = {
      numDefDice: numeroDadi,
      username: difendente,
    };

    appController.defend(idPartita, oggettoDifesa);
  };

  return (
    <Container className="bg-secondary border rounded shadow">
      <Alert variant="danger">Sei sotto attacco</Alert>
      <div>
        <Button onClick={handleDice}>dado 1</Button>
        <Button onClick={handleDice}>dado 2</Button>
        <Button onClick={handleDice}>dado 3</Button>
      </div>
      <div>
        <Button onClick={handleDefense}>Difendi</Button>
      </div>
    </Container>
  );
}

export default ConsoleDifesa;
