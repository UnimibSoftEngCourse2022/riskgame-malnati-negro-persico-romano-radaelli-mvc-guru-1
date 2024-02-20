import { useState } from "react";
import { Container, Button, Nav, Navbar } from "react-bootstrap";
import { GiInvertedDice3 } from "react-icons/gi";

function Console({ carriTerritorio }) {
  const [dadiSelezionati, setDadiSelezionati] = useState(0);

  const handleDiceClick = (numDadi) => {
    setDadiSelezionati((prevDadiSelezionati) => prevDadiSelezionati + numDadi);
  };

  return (
    <Container className="bg-secondary border rounded shadow">
      <p>console di gioco</p>
      <Container fluid>
        <Navbar
          variant="dark"
          className="flex-row"
          style={{ width: "100%", height: "100%" }}
        >
          <Navbar.Brand>
            <Button variant="success">Mostra obbiettivo</Button>
          </Navbar.Brand>
          <Nav className="flex-column">
            <Button variant="primary">Mostra carte</Button>{" "}
          </Nav>
        </Navbar>
      </Container>
      <Container>
        {carriTerritorio && carriTerritorio === 1 && (
          <button className="border rounded" onClick={() => handleDiceClick(1)}>
            <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
          </button>
        )}
        {carriTerritorio && carriTerritorio === 2 && (
          <>
            <button
              className="border rounded"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded bg-danger"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
          </>
        )}

        {carriTerritorio && carriTerritorio > 3 && (
          <>
            <button className="" onClick={() => handleDiceClick(1)}>
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button className="" onClick={() => handleDiceClick(1)}>
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
          </>
        )}
      </Container>
      <Container>
        <span> attacchi con</span>
        <span>{dadiSelezionati}</span>
        <span>dadi</span>
      </Container>
      <Button className="bg-danger">Attacca</Button>
      <Button className="bg-primary">Difendi</Button>
      <p>dadi attaccante</p>
    </Container>
  );
}
export default Console;
