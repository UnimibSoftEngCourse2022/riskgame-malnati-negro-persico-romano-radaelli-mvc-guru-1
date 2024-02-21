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
    // Construct the attack request body
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

    // Call the AppController method, replace 'performAttack' with your method name
    AppController.attack(game.id, attackBody);
    console.log("Attacco effettuato");
  };

  const handleDiceClick = (numDadi) => {
    setDadiSelezionati((prevState) => {
      // Controlla se il dado è già stato selezionato
      if (prevState.includes(numDadi)) {
        // Rimuove il dado dai selezionati
        return prevState.filter((dado) => dado !== numDadi);
      } else {
        // Aggiunge il dado ai selezionati
        return [...prevState, numDadi];
      }
    });
  };

  const renderDefendButton = () => {
    /*  if(game.currentTurn){
		        const isDefender = game.currentTurn.defenseTerritory?.idOwner === player.userName;
		        const shouldShowDefendButton = !territoryAttack && !territoryDefense && isDefender;
		
		        if (shouldShowDefendButton) {
		            return <Button className="bg-warning">Difendi</Button>;
		        }
		
		        return null;
		        }*/
  };

  const renderDadi = () => {
    if (!territoryAttack || !territoryDefense) {
      return null;
    }

    let numDadi = 0;

    if (carriTerritorio === 1) {
      // Messaggio per non poter attaccare con un solo carro armato
      return <p>Non puoi attaccare con una truppa sul territorio.</p>;
    }
    if (carriTerritorio === 2) {
      numDadi = 1;
    }

    if (carriTerritorio === 3) {
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
