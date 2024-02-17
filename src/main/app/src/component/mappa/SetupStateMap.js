import React, { useState, useEffect } from "react";
import SvgMap from "./SvgMap";

function SetUpStateMap({ idPlayer, giocatori, game }) {
  const [player, setPlayer] = useState(null);
  const [troopAssignments, setTroopAssignments] = useState({});
  const [troopsToAssign, setTroopsToAssign] = useState(0);
  const [mappa, setMappa] = useState([]);
  const [myTerritories, setMyTerritories] = useState({});

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
      // Inizializza le assegnazioni delle truppe per ogni territorio
      const territoriGiocatore = giocatori
        .find((p) => p.userName === idPlayer)
        .territories.map((territory) => ({
          name: territory.name,
        }));
      setMyTerritories(territoriGiocatore);
      console.log("territoriGiocatore", territoriGiocatore);

      const initialAssignments = currentPlayer.territories.reduce(
        (acc, territory) => {
          acc[territory.name] = 0;
          return acc;
        },
        {}
      );
      setTroopAssignments(initialAssignments);

      // Calcola il numero di truppe assegnabili in base al numero di giocatori
      const troopsBasedOnPlayers = { 2: 40, 3: 35, 4: 30, 5: 25, 6: 20 };
      setTroopsToAssign(troopsBasedOnPlayers[giocatori.length] || 20);
    }
  }, [giocatori, idPlayer]);

  const handleInputChange = (territoryName, troops) => {
    setTroopAssignments((prev) => ({
      ...prev,
      [territoryName]: troops,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // funzione per calcolare numero di truppe dall oggetto
    const totalTroops = Object.values(troopAssignments).reduce(
      (acc, val) => acc + Number(val),
      0
    );

    if (totalTroops > troopsToAssign) {
      alert(
        `Il numero totale di truppe assegnate supera il limite di ${troopsToAssign}. Hai assegnato ${totalTroops} truppe.`
      );
    } else {
      console.log("Assegnazioni delle truppe:", troopAssignments);
      // Qui puoi procedere con l'invio delle assegnazioni, ad esempio tramite una chiamata API
    }
  };

  if (!player) {
    return <div>Caricamento delle informazioni del giocatore...</div>;
  }

  // Callback che gestisce il click su un territorio
  const handleTerritoryClick = (territoryName) => {
    console.log("hai clicckato");
    if (troopsToAssign > 0) {
      setTroopAssignments((prevAssignments) => {
        const currentTroops = prevAssignments[territoryName] || 0;
        return {
          ...prevAssignments,
          [territoryName]: currentTroops + 1,
        };
      });
      console.log("currentTroops", troopAssignments);
      setTroopsToAssign(troopsToAssign - 1);
    } else {
      alert("Non hai più truppe da assegnare");
    }
  };
  return (
    <div>
      <p>Sei il giocatore: {player.userName}</p>
      <form onSubmit={handleSubmit}>
        <h3>
          Assegna truppe ai tuoi territori (Totale truppe disponibili:{" "}
          {troopsToAssign})
        </h3>
        {player.territories?.map((territory) => (
          <div key={territory.name}>
            <label>
              {territory.name}:
              <input
                type="number"
                min="1"
                value={troopAssignments[territory.name]}
                onChange={(e) =>
                  handleInputChange(territory.name, e.target.value)
                }
              />
            </label>
          </div>
        ))}
        <SvgMap
          paths={mappa}
          gioco={game}
          onTerritoryClick={handleTerritoryClick}
          truppeAssegnate={troopAssignments}
        />
        ;<button type="submit">Conferma assegnazioni</button>
      </form>
    </div>
  );
}

export default SetUpStateMap;
