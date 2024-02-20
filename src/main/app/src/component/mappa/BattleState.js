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
  const [territorioAttacante, setTerritorioAttacante] = useState("");
  const [territorioDifensore, setTerritorioDifensore] = useState("");
  const [territoriVicini, setTerritoriVicini] = useState();
  const [objective, setObjective] = useState();
  const [showAlert, setShowAlert] = useState(true);

  console.log("game battle state", game);

  useEffect(() => {
    const pathD = [];
    const newTroopAssignments = {};

    setIsPlayersTurn(game.currentTurn.player.userName === idPlayer);

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

  const handleTerritoryClick = (territoryName, event) => {
    console.log("tipo di evento on click", event);

    if (event === "addNonOwned") {
      console.log("Click destro");
      console.log("Clicked territory difensore:", territoryName);
      setTerritorioDifensore(territoryName);
    } else if (event === "add") {
      console.log("Click sinistro");
      console.log("Clicked territory attaccante:", territoryName);
      const armateDelTerritorio = troopAssignments[territoryName];
      console.log("Clicked armate territory:", armateDelTerritorio);
      setArmateTerritorio(armateDelTerritorio);
      setTerritorioAttacante(territoryName);
    }

    const territorio = player.territories.find((t) => t.name === territoryName);
    console.log("Territorio  trovato", territorio);

    const continenteTrovato = territorio.continent;
    console.log("continente trovato", continenteTrovato);

    const neighbors = game.continents
      .find((c) => c.continentId === continenteTrovato)
      .territories.find((t) => t.name === territorio.name).neighbors;

    setTerritoriVicini(neighbors);
    console.log("territoriVicini", neighbors);
  };
  return (
    <div style={{ width: "100%", height: "100%" }}>
      <div>
        <p>componente battle state</p>
      </div>
      {isPlayersTurn ? (
        <>
          <SvgMap
            paths={mappa}
            gioco={game}
            onTerritoryClick={handleTerritoryClick}
            truppeAssegnate={troopAssignments}
            territoryAttack={territorioAttacante}
            territoryDefense={territorioDifensore}
            territoryNeighbors={territoriVicini}
          />
          <Console
            carriTerritorio={armateTerritorio}
            territoryAttack={territorioAttacante}
            territoryDefense={territorioDifensore}
          />
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
