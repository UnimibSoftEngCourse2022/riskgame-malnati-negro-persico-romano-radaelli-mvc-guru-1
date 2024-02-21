import React, { useEffect, useState } from "react";

import SvgMap from "./SvgMap";
import Console from "../mappa/Console";
import { Alert, Modal, Button } from "react-bootstrap";
import PartitaObserverSingleton from "../../application/PartitaObserverSingleton";

function BattleState({ idPlayer, game }) {
  const [mappa, setMappa] = useState([]);
  const [player, setPlayer] = useState();
  const [troopAssignments, setTroopAssignments] = useState({});
  const [armateTerritorio, setArmateTerritorio] = useState(0);
  const [isPlayersTurn, setIsPlayersTurn] = useState(true);
  const [territorioAttacante, setTerritorioAttacante] = useState("");
  const [territorioDifensore, setTerritorioDifensore] = useState("");
  const [territoriVicini, setTerritoriVicini] = useState();
  const [sxSelected, setSxSelected] = useState(false); // Aggiungi questo stato
  const [territoriAttaccabili, setTerritoriAttaccabili] = useState([]);
  const [objective, setObjective] = useState();
  const [showAlert, setShowAlert] = useState(true);
  const [esiti, setEsiti] = useState({});
  const [showEsiti, setShowEsiti] = useState(false);

  const handleClose = () => setShowEsiti(false);

  useEffect(() => {
    function updateEsiti(esiti) {
      console.log("Esiti in console: ", esiti);
      setEsiti(esiti);
      setShowEsiti(true);
    }

    PartitaObserverSingleton.addListenerEsiti(updateEsiti);

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
    return () => {
      PartitaObserverSingleton.removeListenerEsiti(updateEsiti);
    };
  }, [idPlayer, game]);

  console.log("mappa battle state", mappa);
  console.log("truppe assegnate", troopAssignments);

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
      {
        <Modal show={showEsiti} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Esito dell'Attacco</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {esiti.isConquered ? (
              <p>Il territorio è stato conquistato.</p>
            ) : (
              <p>Il territorio non è stato conquistato.</p>
            )}
            <p>Truppe perse dall'attaccante: {esiti.lostAttTroops}</p>
            <p>Truppe perse dal difensore: {esiti.lostDefTroops}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Chiudi
            </Button>
          </Modal.Footer>
        </Modal>
      }
    </div>
  );
}

export default BattleState;
