import { useEffect, useState } from "react";
import { Button, Container } from "react-bootstrap";
import SvgMap from "./SvgMap";

function StartTurnState({ idPlayer, game }) {
  console.log("idPlayer in StartTurnState", idPlayer);
  console.log("game in StartTurnState", game);

  const [comboCard, setComboCard] = useState();
  const [objective, setObjective] = useState();
  const [renderMap, setRenderMap] = useState(false);
  const [mappa, setMappa] = useState([]);
  const [troopAssignments, setTroopAssignments] = useState(0);

  useEffect(() => {
    const player = game.players?.find((p) => p.userName === idPlayer);
    console.log("sei il giocatore nel turno", player);

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

  const handleClick = () => {
    setRenderMap(true);
  };

  const handleTerritoryClick = () => {
    console.log("chiamata a territory click");
  };

  return (
    <div>
      <p>componente di inizio turno</p>
      {!renderMap ? (
        <Container className="bg-dark border rounded shadow">
          {comboCard && comboCard.length > 3 ? (
            <div>hai le carte combo</div>
          ) : (
            <div>
              <p>non hai carte combo da usare</p>
            </div>
          )}

          <div>
            <p className="text-white">vuoi usare le carte ?</p>
            <Button>Si</Button>
            <Button onClick={handleClick}>No</Button>
          </div>
        </Container>
      ) : (
        <div>
          map
          <SvgMap
            paths={mappa}
            gioco={game}
            truppeAssegnate={troopAssignments}
            onTerritoryClick={handleTerritoryClick}
          />
        </div>
      )}
    </div>
  );
}

export default StartTurnState;
