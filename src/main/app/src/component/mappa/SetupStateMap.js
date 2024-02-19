import React, { useState, useEffect } from "react";

import AppController from "../../application/AppController";
import SvgMap from "./SvgMap";
import { Alert, Button, Container } from "react-bootstrap";

function SetUpStateMap({ idPlayer, giocatori, game }) {
  const [player, setPlayer] = useState(null);
  const [troopAssignments, setTroopAssignments] = useState({});
  const [troopsToAssign, setTroopsToAssign] = useState(0);
  const [mappa, setMappa] = useState([]);
  const [initialTroopsToAssign, setInitialTroopsToAssign] = useState(0);
  const [colorPlayer, setColorPlayer] = useState("");

  useEffect(() => {
    const pathD = giocatori.flatMap((player) =>
      player.territories.map((territory) => ({
        d: territory.svgPath,
        name: territory.name,
      }))
    );
    setMappa(pathD);

    const currentPlayer = giocatori.find((p) => p.userName === idPlayer);
    setPlayer(currentPlayer);

    console.log("player in setup", currentPlayer);
    console.log("game in setup", game);
    setColorPlayer(currentPlayer.color);

    if (currentPlayer) {
      const initialAssignments = currentPlayer.territories.reduce(
        (acc, territory) => {
          acc[territory.name] = 1;
          return acc;
        },
        {}
      );
      setTroopAssignments(initialAssignments);

      // Calcola il numero di truppe assegnabili in base al numero di giocatori
      const troopsBasedOnPlayers = { 2: 40, 3: 35, 4: 30, 5: 25, 6: 20 };
      const totalTerritories = currentPlayer.territories.length; // Numero totale di territori del giocatore corrente
      const troopsToAssignInitially =
        troopsBasedOnPlayers[giocatori.length] || 20; // Totale truppe basato sul numero di giocatori

      // Sottrai il numero totale di territori dal totale di truppe assegnabili per tenere conto della truppa iniziale per territorio
      setTroopsToAssign(troopsToAssignInitially - totalTerritories);
      setInitialTroopsToAssign(troopsToAssignInitially);
    }
  }, [giocatori, idPlayer]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const totalTroops = Object.values(troopAssignments).reduce(
      (acc, val) => acc + Number(val),
      0
    );

    if (totalTroops !== initialTroopsToAssign) {
      alert(
        `Devi assegnare esattamente ${troopsToAssign} truppe. Attualmente ne hai assegnate ${totalTroops}.`
      );
      return;
    }

    const setUpBody = {
      username: idPlayer,
      territories: Object.entries(troopAssignments).map(([name, troops]) => ({
        name,
        troops: Number(troops),
      })),
    };

    console.log("Assegnazioni delle truppe per il setup:", setUpBody);
    AppController.setUpPartita(player.gameId, setUpBody);
  };

  if (!player) {
    return <div>Caricamento delle informazioni del giocatore...</div>;
  }

  // Callback che gestisce il click su un territorio
  const handleTerritoryClick = (territoryName, action) => {
    setTroopAssignments((prevAssignments) => {
      const currentTroops = prevAssignments[territoryName];
      if (action === "add") {
        if (troopsToAssign <= 0) {
          alert("Non puoi assegnare più truppe. Hai raggiunto il limite.");
          return prevAssignments;
        }
        setTroopsToAssign(troopsToAssign - 1);
        return { ...prevAssignments, [territoryName]: currentTroops + 1 };
      } else if (action === "remove") {
        if (currentTroops <= 1) {
          alert("Non ci sono truppe da rimuovere in questo territorio.");
          return prevAssignments;
        }
        setTroopsToAssign(troopsToAssign + 1);
        return { ...prevAssignments, [territoryName]: currentTroops - 1 };
      }
      return prevAssignments;
    });
  };

  return (
    <Container fluid className="">
      {console.log("player in setup", player)}
      {console.log("player name in setup", player.userName)}
      {console.log("player color in setup", player.color)}
      {player && <Alert>{player.objective.objective}</Alert>}
      {console.log("player objective in setup", player.objective.objective)}

      <div className="d-flex flew-row justify-content-center align-items-center">
        <span>Il colore delle tue truppe è il </span>
        <div
          style={{
            backgroundColor: `${colorPlayer}`,
            width: "15px",
            height: "15px",
            marginLeft: "3px",
          }}
        ></div>
      </div>

      <p>
        posiziona le truppe in base al tuo obbiettivo, utilizza il click
        sinistro per aggiungere delle truppe al territorio e utilizza il click
        destro per rimuoverle
      </p>
      <Container>
        <SvgMap
          paths={mappa}
          gioco={game}
          onTerritoryClick={handleTerritoryClick}
          truppeAssegnate={troopAssignments}
        />
        <Button className="m-2" onClick={handleSubmit}>
          Conferma assegnazioni
        </Button>
      </Container>
    </Container>
  );
}

export default SetUpStateMap;
