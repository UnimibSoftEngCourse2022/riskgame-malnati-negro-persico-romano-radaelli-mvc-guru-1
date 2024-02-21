import { useState } from "react";
import { Container, Button, Nav, Navbar } from "react-bootstrap";
import { GiInvertedDice3 } from "react-icons/gi";
import AppController from "../../application/AppController";

function Console({
  carriTerritorio,
  territoryAttack,
  territoryDefense,
  game,
  player,
}) {
  const [dadiSelezionati, setDadiSelezionati] = useState([]);

  const handleAttack = () => {
    const defenderUsername = game.players.find((player) =>
      player.territories.some(
        (territory) => territory.name === territoryDefense
      )
    )?.userName;

    const attackBody = {
      attackerTerritory: {
        nameTerritory: territoryAttack,
        username: player.userName,
      },
      defenderTerritory: {
        nameTerritory: territoryDefense,
        username: defenderUsername,
      },
      numAttDice: dadiSelezionati.length,
    };

    AppController.attack(game.id, attackBody);
  };

  const handleDiceClick = (numDadi) => {
    setDadiSelezionati((prevState) => {
      if (prevState.includes(numDadi)) {
        return prevState.filter((dado) => dado !== numDadi);
      } else {
        return [...prevState, numDadi];
      }
    });
  };

  const renderDadi = () => {
    if (!territoryAttack || !territoryDefense) {
      return null;
    }

    let numDadi = 0;

    if (carriTerritorio === 1) {
      return <p>Non puoi attaccare con una truppa sul territorio.</p>;
    }
    if (carriTerritorio === 2) {
      numDadi = 1;
    }

    if (carriTerritorio == 3) {
      numDadi = 2;
    } else {
      numDadi = 3;
    }

    return (
      <>
        {[...Array(numDadi)].map((_, index) => (
          <button
            key={index}
            onClick={() => handleDiceClick(index + 1)}
            className={`border rounded ${
              dadiSelezionati.includes(index + 1) ? "bg-danger" : ""
            }`}
          >
            <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
          </button>
        ))}
      </>
    );
  };

  return (
    <Container className="bg-secondary border rounded shadow">
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
      <Container>{renderDadi()}</Container>
      <Container>
        {territoryAttack && <span>{territoryAttack}</span>}
        {territoryDefense && <span> attacca {territoryDefense}</span>}
        {territoryAttack && territoryDefense && dadiSelezionati.length > 0 && (
          <div>
            <span>Attacchi con {dadiSelezionati.length} dadi</span>
            <div>
              <p>Sei sicuro di voler lanciare l'attacco?</p>
              <Button className="bg-danger" onClick={handleAttack}>
                Attacca
              </Button>
            </div>
          </div>
        )}
        {renderDefendButton()}
      </Container>
    </Container>
  );
}
export default Console;
