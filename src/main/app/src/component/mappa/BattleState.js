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

  const [sxSelected, setSxSelected] = useState(false);
  const [territoriAttaccabili, setTerritoriAttaccabili] = useState([]);

  const [esiti, setEsiti] = useState({});
  const [showEsiti, setShowEsiti] = useState(false);
  const [isDefenderTerritory, setIsDefenderTerritory] = useState(null);
  const [playerUnderAttack, setPlayerUnderAttack] = useState();
  const [countryUnderAttack, setCountryUnderAttack] = useState("");
  const [IdGioco, setIdGioco] = useState();

  console.log(esiti);
  console.log(showEsiti);
  console.log(countryUnderAttack);

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

      const CountrysottoAttacco = game.currentTurn.defenderTerritory.idOwner;
      setCountryUnderAttack(CountrysottoAttacco);
    }

    return () => {
      PartitaObserverSingleton.removeListenerEsiti(updateEsiti);
    };
  }, [idPlayer, game, isDefenderTerritory]);

  const handleTerritoryClick = (territoryName, event) => {
    console.log("tipo di evento on click", event);

    if (event === "add") {
      const territorio = player.territories.find(
        (t) => t.name === territoryName
      );
      const continenteTrovato = territorio.continent;
      setTerritorioAttacante(territoryName);
      const armateDelTerritorio = troopAssignments[territoryName];
      setArmateTerritorio(armateDelTerritorio);
      setSxSelected(true);
      setTerritorioDifensore("");
      const neighbors = game.continents
        .find((c) => c.continentId === continenteTrovato)
        .territories.find((t) => t.name === territoryName).neighbors;
      const territoriGiocatore = player.territories.map((t) => t.name);
      const attackableTerritory = neighbors.filter(
        (neighbor) => !territoriGiocatore.includes(neighbor)
      );
      setTerritoriAttaccabili(attackableTerritory);
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