import React, { useEffect, useState } from "react";

import SvgMap from "./SvgMap";
import Console from "../mappa/Console";
import { Alert } from "react-bootstrap";
import PartitaObserverSingleton from "../../application/PartitaObserverSingleton";
import ConsoleDifesa from "./ConsoleDifesa";

function BattleState({ idPlayer, game }) {
  const [mappa, setMappa] = useState([]);
  const [player, setPlayer] = useState();
  const [troopAssignments, setTroopAssignments] = useState({});
  const [armateTerritorio, setArmateTerritorio] = useState(0);
  const [isPlayersTurn, setIsPlayersTurn] = useState(true);
  const [territorioAttacante, setTerritorioAttacante] = useState("");
  const [territorioDifensore, setTerritorioDifensore] = useState("");

  const [sxSelected, setSxSelected] = useState(false); // Aggiungi questo stato
  const [territoriAttaccabili, setTerritoriAttaccabili] = useState([]);

  const [setEsiti] = useState({});
  const [setShowEsiti] = useState(false);
  const [isDefenderTerritory, setIsDefenderTerritory] = useState(null);
  const [playerUnderAttack, setPlayerUnderAttack] = useState();
  const [setCountryUnderAttack] = useState("");
  const [IdGioco, setIdGioco] = useState();

  // const handleClose = () => setShowEsiti(false);

  useEffect(() => {
    function updateEsiti(esiti) {
      console.log("Esiti in console: ", esiti);
      setEsiti(esiti);
      setShowEsiti(true);
    }

    setIdGioco(game.id);

    PartitaObserverSingleton.addListenerEsiti(updateEsiti);

    const pathD = [];
    const newTroopAssignments = {};

    setIsPlayersTurn(game.currentTurn.player.userName === idPlayer);
    const isDifTerr = game.currentTurn.defenderTerritory;
    setIsDefenderTerritory(isDifTerr);
    console.log("isDifTerr", isDifTerr);

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

    if (isDefenderTerritory) {
      const sottoAttacco = game.currentTurn.defenderTerritory.idOwner;
      setPlayerUnderAttack(sottoAttacco);
      console.log("player sottoAttacco", sottoAttacco);

      const CountrysottoAttacco = game.currentTurn.defenderTerritory.idOwner;
      setCountryUnderAttack(CountrysottoAttacco);
      console.log("country Under Attack", CountrysottoAttacco);
    }

    return () => {
      PartitaObserverSingleton.removeListenerEsiti(updateEsiti);
    };
  }, [idPlayer, game]);

  console.log("playerUnderAttack", playerUnderAttack);

  {
    player && console.log("player in battleState", player);
  }
  {
    player && console.log("game in battleState", game);
  }

  const handleTerritoryClick = (territoryName, event) => {
    console.log("tipo di evento on click", event);

    if (event === "add") {
      const territorio = player.territories.find(
        (t) => t.name === territoryName
      );
      const continenteTrovato = territorio.continent;
      console.log("Continente trovato", continenteTrovato);
      console.log("Click sinistro");
      setTerritorioAttacante(territoryName);
      console.log("Clicked territory attaccante:", territorioAttacante);
      const armateDelTerritorio = troopAssignments[territoryName];
      console.log("Clicked armate territory:", armateDelTerritorio);
      setArmateTerritorio(armateDelTerritorio);
      setSxSelected(true);
      setTerritorioDifensore("");
      const neighbors = game.continents
        .find((c) => c.continentId === continenteTrovato)
        .territories.find((t) => t.name === territoryName).neighbors;
      console.log("neighbors", neighbors);
      const territoriGiocatore = player.territories.map((t) => t.name);
      const attackableTerritory = neighbors.filter(
        (neighbor) => !territoriGiocatore.includes(neighbor)
      );
      setTerritoriAttaccabili(attackableTerritory);
      console.log("territori attaccabili", territoriAttaccabili);
    } else if (
      event === "addNonOwned" &&
      sxSelected &&
      territoriAttaccabili.includes(territoryName)
    ) {
      console.log("Click destro");
      setTerritorioDifensore(territoryName);
      console.log("Clicked territory difensore:", territorioDifensore);
      setSxSelected(false);
    }
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
            territoryNeighbors={territoriAttaccabili}
            sxSelected={sxSelected}
          />
          <Console
            carriTerritorio={armateTerritorio}
            territoryAttack={territorioAttacante}
            territoryDefense={territorioDifensore}
            game={game}
            player={player}
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
          <Console
            carriTerritorio={armateTerritorio}
            game={game}
            player={player}
          />
        </>
      )}

      {isDefenderTerritory &&
        player.userName === playerUnderAttack &&
        console.log(
          "sei tu sotto attacco",
          player.userName === playerUnderAttack
        )}
      {isDefenderTerritory && player.userName === playerUnderAttack && (
        <div>
          <ConsoleDifesa idPartita={IdGioco} difendente={playerUnderAttack} />
        </div>
      )}
    </div>
  );
}

export default BattleState;
