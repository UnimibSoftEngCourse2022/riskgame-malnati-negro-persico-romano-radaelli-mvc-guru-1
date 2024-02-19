import { useEffect, useState } from "react";
import { Button, Container } from "react-bootstrap";
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
  //Truppe inserite in questo componente
  const [troopAssignments, setTroopAssignments] = useState({});
  //Truppe gia inserite dal setup
  const [initialTroopCounts, setInitialTroopCounts] = useState({});
  //Truppe massime che si possono inserire
  const [troopsToAdd, setTroopsToAdd] = useState(0);

  useEffect(() => {

	//E il turno del giocatore?  
	setIsPlayersTurn(game.currentTurn.player.userName === idPlayer);
	
	//setto le truppe iniziali relativi ai territori
	const initialCounts = {};
  	game.players.forEach(player => {
    	player.territories.forEach(territory => {
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

    setObjective(game.deckObjective.cards[0].objective);
  }, [game, idPlayer]);

  console.log("objective", objective);
  console.log("comboCard", comboCard);
  
  //Click bottone carte combo
  const handleClick = () => {
    setRenderMap(true);
  };
  
  //Click territorio
  const handleTerritoryClick = (territoryName, action) => {
    if (!isPlayersTurn) return;

    setTroopAssignments((prev) => {
      const currentAssigned = prev[territoryName] || 0;
      let updatedTroops = currentAssigned;

      if (action === 'add') {
        if (troopsToAdd > 0) {
          updatedTroops += 1;
          setTroopsToAdd(troopsToAdd - 1);
        } else {
          alert("Non puoi assegnare più truppe di quelle disponibili per questo turno.");
          return prev;
        }
      } else if (action === 'remove') {
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
      username : idPlayer,
      territories: Object.entries(combinedTroops).map(([name, troops]) => ({
        name,
        troops: Number(troops),
      })), // Usa l'oggetto combinedTroops per ottenere le truppe finali
    };
    
    AppController.setUpTurno(game.id, confirmationData);
  };
  
  const combinedTroops = Object.keys(initialTroopCounts).reduce((acc, territory) => {
  const initialCount = initialTroopCounts[territory] || 0;
  const assignmentChange = troopAssignments[territory] || 0;
  acc[territory] = initialCount + assignmentChange;
  return acc;
}, {});

  return (
	  console.log("playerTurn", isPlayersTurn),
    <div>
      {isPlayersTurn && (
        !renderMap ? (
          <Container className="bg-dark border rounded shadow p-2">
            {comboCard && comboCard.length > 3 ? (
              <div>hai le carte combo</div>
            ) : (
              <div>
                <p>non hai carte combo da usare</p>
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
            <SvgMap
              paths={mappa}
              gioco={game}
              truppeAssegnate={combinedTroops}
              onTerritoryClick={handleTerritoryClick}
            />
            <Button onClick={handleConfirmClick} className="mt-3">Conferma Inserimenti</Button>
            <Container className="bg-secondary">console di gioco</Container>
          </div>
        )
      )}
      {!isPlayersTurn && (
        // Qui mostri solo la mappa ai giocatori che non sono nel loro turno
        <SvgMap
          paths={mappa}
          gioco={game}
          truppeAssegnate={combinedTroops}
          // Puoi anche passare una funzione vuota o non passare la prop 'onTerritoryClick'
          onTerritoryClick={() => {}}
        />
      )}
    </div>
  );
}

export default StartTurnState;
