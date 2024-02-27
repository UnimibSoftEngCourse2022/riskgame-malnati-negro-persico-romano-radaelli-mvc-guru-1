import React, { useEffect, useState } from "react";
import { Alert, Button, Container } from "react-bootstrap";
import appController from "../../application/AppController";

function ConsoleDifesa({ idPartita, difendente }) {
  const [numeroDadi, setNumeroDadi] = useState(0);
  const [stateDefenser, setStateDefenser] = useState("");
  
  useEffect(() => {
  setStateDefenser("sei sotto attacco");
  }, [stateDefenser]);

  const handleDice = (num) => {
    // Imposta il numero di dadi in base al numero associato al dado
    setNumeroDadi(num);
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
      <Alert variant="danger">{stateDefenser}</Alert>
      <div>
        <Button onClick={() => handleDice(1)}>dado 1</Button>
        <Button onClick={() => handleDice(2)}>dado 2</Button>
        <Button onClick={() => handleDice(3)}>dado 3</Button>
      </div>
      <div>
        <Button onClick={handleDefense}>Difendi</Button>
      </div>
    </Container>
  );
}

export default ConsoleDifesa;
