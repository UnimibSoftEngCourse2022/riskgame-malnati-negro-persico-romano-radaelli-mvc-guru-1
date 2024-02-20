import React, { useEffect, useState } from "react";

import SvgMap from "./SvgMap";
import Console from "../mappa/Console";
import { Alert } from "react-bootstrap";

function BattleState({ idPlayer, game }) {
  const [mappa, setMappa] = useState([]);
  const [player, setPlayer] = useState();
  const [troopAssignments, setTroopAssignments] = useState({});
  const [armateTerritorio, setArmateTerritorio] = useState(0);
  const [isPlayersTurn, setIsPlayersTurn] = useState(true);

  console.log("game", game);

  useEffect(() => {
    const pathD = [];
    const newTroopAssignments = {};

    // il turno corrente arriva null dal backend
    // setIsPlayersTurn(game.currentTurn.player.userName === idPlayer);

    game.players.forEach((player) => {
      player.territories.forEach((territory) => {
        pathD.push({
          d: territory.svgPath,
          name: territory.name,
        });

        newTroopAssignments[territory.name] = territory.armies;
      });
    });

    setMappa(pathD);
    setTroopAssignments(newTroopAssignments);

    const currentPlayer = game.players.find((p) => p.userName === idPlayer);
    setPlayer(currentPlayer);
  }, [idPlayer, game]);

  console.log("mappa battle state", mappa);
  console.log("truppe assegnate", troopAssignments);
  console.log("giocatore", player);

  const handleTerritoryClick = (territoryName, event) => {
    console.log("tipo di evento on click", event);
    console.log("Click sinistro");
    console.log("Clicked territory attaccante:", territoryName);
    const armateDelTerritorio = troopAssignments[territoryName];
    console.log("Clicked armate territory:", armateDelTerritorio);
    setArmateTerritorio(armateDelTerritorio);

    if (event.type === "contextmenu") {
      console.log("Click destro");
      console.log("Clicked territory difensore:", territoryName);
    }
  };

  return (
    <div style={{ width: "100%", height: "100%" }}>
      <p>componente battle state</p>
      {isPlayersTurn ? (
        <>
          <SvgMap
            paths={mappa}
            gioco={game}
            onTerritoryClick={handleTerritoryClick}
            truppeAssegnate={troopAssignments}
          />
          <Console carriTerritorio={armateTerritorio} />
        </>
      ) : (
        <>
          <Alert variant="danger" className="text-danger">
            attendi il tuo turno
          </Alert>

          <SvgMap
            paths={mappa}
            gioco={game}
            onTerritoryClick={() => {}}
            truppeAssegnate={troopAssignments}
          />
          <Console carriTerritorio={armateTerritorio} />
        </>
      )}
    </div>
  );
}

export default BattleState;
