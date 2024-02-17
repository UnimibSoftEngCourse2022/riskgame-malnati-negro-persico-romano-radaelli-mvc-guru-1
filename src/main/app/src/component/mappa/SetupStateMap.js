import React, { useState, useEffect } from "react";

function SetUpStateMap({ idPlayer, giocatori }) {
  const [player, setPlayer] = useState(null);
  const [troopAssignments, setTroopAssignments] = useState({});
  const [troopsToAssign, setTroopsToAssign] = useState(0);

  useEffect(() => {
    console.log("Props ricevute:", { idPlayer, giocatori });
    const currentPlayer = giocatori.find(p => p.userName === idPlayer);
    setPlayer(currentPlayer);

    if (currentPlayer) {
      // Inizializza le assegnazioni delle truppe per ogni territorio
      const initialAssignments = currentPlayer.territories.reduce((acc, territory) => {
        acc[territory.name] = 0;
        return acc;
      }, {});
      setTroopAssignments(initialAssignments);

      // Calcola il numero di truppe assegnabili in base al numero di giocatori
      const troopsBasedOnPlayers = { 2: 40, 3: 35, 4: 30, 5: 25, 6: 20 };
      setTroopsToAssign(troopsBasedOnPlayers[giocatori.length] || 20);
    }
  }, [giocatori, idPlayer]);

  const handleInputChange = (territoryName, troops) => {
    setTroopAssignments(prev => ({
      ...prev,
      [territoryName]: troops
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const totalTroops = Object.values(troopAssignments).reduce((acc, val) => acc + Number(val), 0);
    
	    if (totalTroops > troopsToAssign) {
	      alert(`Il numero totale di truppe assegnate supera il limite di ${troopsToAssign}. Hai assegnato ${totalTroops} truppe.`);
    } else {
      console.log("Assegnazioni delle truppe:", troopAssignments);
      // Qui puoi procedere con l'invio delle assegnazioni, ad esempio tramite una chiamata API
    }
  };

  if (!player) {
    return <div>Caricamento delle informazioni del giocatore...</div>;
  }

  return (
    <div>
      <p>Sei il giocatore: {player.userName}</p>
      <form onSubmit={handleSubmit}>
        <h3>Assegna truppe ai tuoi territori (Totale truppe disponibili: {troopsToAssign})</h3>
        {player.territories?.map(territory => (
          <div key={territory.name}>
            <label>
              {territory.name}: 
              <input
                type="number"
                min="1"
                value={troopAssignments[territory.name]}
                onChange={(e) => handleInputChange(territory.name, e.target.value)}
              />
            </label>
          </div>
        ))}
        <button type="submit">Conferma assegnazioni</button>
      </form>
    </div>
  );
}

export default SetUpStateMap;
