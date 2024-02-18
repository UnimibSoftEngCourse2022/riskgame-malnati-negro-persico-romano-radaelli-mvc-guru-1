import React, { useState, useEffect } from "react";

import AppController from "../../application/AppController";
import SvgMap from "./SvgMap";


function SetUpStateMap({ idPlayer, giocatori, game }) {
  const [player, setPlayer] = useState(null);
  const [troopAssignments, setTroopAssignments] = useState({});
  const [troopsToAssign, setTroopsToAssign] = useState(0);
  const [mappa, setMappa] = useState([]);
  const [initialTroopsToAssign, setInitialTroopsToAssign] = useState(0);

  useEffect(() => {
    console.log("Props ricevute:", { idPlayer, giocatori });

    const pathD = giocatori.flatMap((player) =>
      player.territories.map((territory) => ({
        d: territory.svgPath,
        name: territory.name,
      }))
    );
    setMappa(pathD);
    console.log("pathD", pathD);

    const currentPlayer = giocatori.find((p) => p.userName === idPlayer);
    setPlayer(currentPlayer);
    console.log("player", currentPlayer);

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
	  const troopsToAssignInitially = troopsBasedOnPlayers[giocatori.length] || 20; // Totale truppe basato sul numero di giocatori

	  // Sottrai il numero totale di territori dal totale di truppe assegnabili per tenere conto della truppa iniziale per territorio
	  setTroopsToAssign(troopsToAssignInitially - totalTerritories);
	  setInitialTroopsToAssign(troopsToAssignInitially); 
    }
  }, [giocatori, idPlayer]);

 const handleSubmit = (e) => {
  e.preventDefault();

  const totalTroops = Object.values(troopAssignments).reduce((acc, val) => acc + Number(val), 0);

  if (totalTroops !== initialTroopsToAssign) {
    alert(`Devi assegnare esattamente ${troopsToAssign} truppe. Attualmente ne hai assegnate ${totalTroops}.`);
    return;
  }
  
  console.log("Sono passato");

  const setUpBody = {
    username: idPlayer, // Assumi che idPlayer sia l'username o id del giocatore
    territories: Object.entries(troopAssignments).map(([name, troops]) => ({
      name, // nome del territorio
      troops: Number(troops) // numero di truppe assegnate
    }))
  };

  console.log("Assegnazioni delle truppe per il setup:", setUpBody);
  AppController.setUpPartita(player.gameId, setUpBody); // Assumi che AppController accetti l'oggetto così strutturato
};


  if (!player) {
    return <div>Caricamento delle informazioni del giocatore...</div>;
  }

  // Callback che gestisce il click su un territorio
  const handleTerritoryClick = (territoryName, action) => {
  setTroopAssignments((prevAssignments) => {
    const currentTroops = prevAssignments[territoryName];
    if (action === 'add') {
      if (troopsToAssign <= 0) {
        alert("Non puoi assegnare più truppe. Hai raggiunto il limite.");
        return prevAssignments; // Esci senza fare modifiche
      }
      setTroopsToAssign(troopsToAssign - 1);
      return { ...prevAssignments, [territoryName]: currentTroops + 1 };
    } else if (action === 'remove') {
      if (currentTroops <= 1) {
        alert("Non ci sono truppe da rimuovere in questo territorio.");
        return prevAssignments; // Esci senza fare modifiche
      }
      setTroopsToAssign(troopsToAssign + 1);
      return { ...prevAssignments, [territoryName]: currentTroops - 1 };
    }
    return prevAssignments; // Per sicurezza, ritorna lo stato precedente se l'azione non è riconosciuta
  });
};

  return (
    <div>
      <p>Sei il giocatore: {player.userName}</p>
        <SvgMap
          paths={mappa}
          gioco={game}
          onTerritoryClick={handleTerritoryClick}
          truppeAssegnate={troopAssignments}
        />
        <button onClick={handleSubmit}>Conferma assegnazioni</button>
    </div>
  );
}

export default SetUpStateMap;
