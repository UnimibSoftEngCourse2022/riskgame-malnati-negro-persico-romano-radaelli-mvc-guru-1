import { useEffect, useState } from "react";
import { Alert, Button, Container } from "react-bootstrap";
import AppController from "../../application/AppController";

import SvgMap from "./SvgMap";

function StartTurnState({ idPlayer, game }) {
  console.log("idPlayer in StartTurnState", idPlayer);
  console.log("game in StartTurnState", game);

  const [comboCard, setComboCard] = useState();
  const [isPlayersTurn, setIsPlayersTurn] = useState(false);
  const [objective, setObjective] = useState();
  const [renderMap, setRenderMap] = useState(false);
  const [mappa, setMappa] = useState([]);

  const [troopAssignments, setTroopAssignments] = useState({});

  const [initialTroopCounts, setInitialTroopCounts] = useState({});

  const [troopsToAdd, setTroopsToAdd] = useState(0);

  useEffect(() => {
    setIsPlayersTurn(game.currentTurn.player.userName === idPlayer);

    const initialCounts = {};
    game.players.forEach((player) => {
      player.territories.forEach((territory) => {
        initialCounts[territory.name] = territory.armies;
      });
    });
    setInitialTroopCounts(initialCounts);
    console.log("initialCounts", initialCounts);
    setTroopsToAdd(game.currentTurn.numberOfTroops);
    console.log("troopsToAdd", game.currentTurn.numberOfTroops);

    const player = game.players?.find((p) => p.userName === idPlayer);

    const pathD = game.players.flatMap((player) =>
      Array.isArray(player.territories)
        ? player.territories.map((territory) => ({
            d: territory.svgPath,
            name: territory.name,
          }))
        : []
    );
    console.log("pathD", pathD);
    setMappa(pathD);

    if (player) {
      const carteComboGiocatore = player.comboCards?.map((c) => c.symbol);
      setComboCard(carteComboGiocatore);
    }

    const isPlayer = game.players?.find((p) => p.userName === idPlayer);
    setObjective(isPlayer.objective.objective);
  }, [game, idPlayer]);

  console.log("objective", objective);
  console.log("comboCard", comboCard);

  const handleClick = () => {
    setRenderMap(true);
  };

  const handleTerritoryClick = (territoryName, action) => {
    if (!isPlayersTurn) return;

    setTroopAssignments((prev) => {
      const currentAssigned = prev[territoryName] || 0;
      let updatedTroops = currentAssigned;

      if (action === "add") {
        if (troopsToAdd > 0) {
          updatedTroops += 1;
          setTroopsToAdd(troopsToAdd - 1);
        } else {
          alert(
            "Non puoi assegnare più truppe di quelle disponibili per questo turno."
          );
          return prev;
        }
      } else if (action === "remove") {
        if (updatedTroops > 0) {
          updatedTroops -= 1;
          setTroopsToAdd(troopsToAdd + 1);
        } else {
          alert("Non puoi rimuovere truppe non assegnate in questo turno.");
          return prev;
        }
      }

      return { ...prev, [territoryName]: updatedTroops };
    });
  };

  //Click conferma inserimenti
  const handleConfirmClick = () => {
    // Qui puoi chiamare il metodo sull'AppController
    // Assicurati di avere accesso a appController tramite props o contesto
    const confirmationData = {
      username: idPlayer,
      territories: Object.entries(combinedTroops).map(([name, troops]) => ({
        name,
        troops: Number(troops),
      })), // Usa l'oggetto combinedTroops per ottenere le truppe finali
    };

    AppController.setUpTurno(game.id, confirmationData);
  };

  const combinedTroops = Object.keys(initialTroopCounts).reduce(
    (acc, territory) => {
      const initialCount = initialTroopCounts[territory] || 0;
      const assignmentChange = troopAssignments[territory] || 0;
      acc[territory] = initialCount + assignmentChange;
      return acc;
    },
    {}
  );

  return (
    console.log("playerTurn", isPlayersTurn),
    (
      <div>
        {isPlayersTurn &&
          (!renderMap ? (
            <Container className="bg-dark border rounded shadow p-2">
              {comboCard && comboCard.length > 3 ? (
                <div>
                  <p className="text-white">hai le carte combo</p>
                </div>
              ) : (
                <div>
                  <p className="text-white">non hai carte combo da usare</p>
                </div>
              )}

              <div className="p-2">
                <p className="text-white">vuoi usare le carte ?</p>
                <Button onClick={handleClick}>Si</Button>
                <Button onClick={handleClick}>No</Button>
              </div>
            </Container>
          ) : (
            <div>
              {troopsToAdd && (
                <Alert variant="success" className="text-success">
                  <span>hai ricevuto</span>
                  <span>{troopsToAdd}</span>
                  <span>truppe</span>
                </Alert>
              )}

              <SvgMap
                paths={mappa}
                gioco={game}
                truppeAssegnate={combinedTroops}
                onTerritoryClick={handleTerritoryClick}
              />
              <Button onClick={handleConfirmClick} className="mt-3">
                Conferma Inserimenti
              </Button>
            </div>
          ))}
        {!isPlayersTurn && (
          <>
            <Alert variant="danger" className="text-danger">
              attendi il tuo turno
            </Alert>
            <SvgMap
              paths={mappa}
              gioco={game}
              truppeAssegnate={combinedTroops}
              // Puoi anche passare una funzione vuota o non passare la prop 'onTerritoryClick'
              onTerritoryClick={() => {}}
            />
          </>
        )}
      </div>
    )
  );
}

export default StartTurnState;
