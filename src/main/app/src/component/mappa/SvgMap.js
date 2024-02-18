import React, { useEffect, useState } from "react";
import { Container, Alert, Tooltip, OverlayTrigger } from "react-bootstrap";
import { useParams } from "react-router-dom";

function SvgMap({ paths, gioco, onTerritoryClick, truppeAssegnate }) {
const handleTerritoryClick = (e, territory, action) => {
    e.preventDefault(); // Questo impedisce la comparsa del menu contestuale del browser
    
    // Chiama direttamente onTerritoryClick con l'azione specifica
    onTerritoryClick(territory, action);
};
  const [myTerritories, setMyTerritories] = useState([]);
  const [myColor, setMyColor] = useState("");
  const [objective, setObjective] = useState("");

  const url = window.location.href;
  const nickname = url.split("/").pop();

  useEffect(() => {
    console.log("sei nel gioco", gioco);
    console.log("nickname", nickname);
    const player = gioco.players.find((p) => p.userName === nickname);
    console.log("sei il giocatore", player);
    if (player) {
      const territoriGiocatore = player.territories.map((t) => t.name);
      setMyTerritories(territoriGiocatore);
      console.log("territoriGiocatore", territoriGiocatore);
      setMyColor(player.color);
      console.log("myColor", myColor);
    }

    setObjective(gioco.deckObjective.cards[0].objective);

    // Debugging logs
    console.log("ID del giocatore corrente:", nickname);
    console.log(
      "Giocatori nella partita:",
      gioco.players.map((p) => p.userName)
    );
  }, [gioco, nickname, myColor]);
  
  const renderTooltip = (props, name) => (
    <Tooltip id="button-tooltip" {...props}>
      {`${name}: ${truppeAssegnate[name] || 0} truppe`}
    </Tooltip>
  );

  return (
    <Container>
      <Alert variant="info">Obiettivo del Gioco: {objective}</Alert>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="100%"
        height="100%"
        viewBox="0 0 1024 650"
        id="mapSvg"
        aria-labelledby="title"
      >
        {paths.map(({ d, name }, index) => {
          const isTerritoryOwned = myTerritories.includes(name);
          const fillColor = isTerritoryOwned ? myColor : "grey";
          return (
			   <OverlayTrigger
              key={index}
              placement="top"
              overlay={(props) => renderTooltip(props, name)}
            >
            <path
              id={name}
              d={d}
              stroke="black"
              fill={fillColor} // Usa il colore specifico del giocatore
              opacity="0.5"
              onClick={(e) => isTerritoryOwned && handleTerritoryClick(e, name, 'add')}
               onContextMenu={(e) => isTerritoryOwned && handleTerritoryClick(e, name, 'remove')}
            >
              <title>{`${name}: ${truppeAssegnate[name] || 0} truppe`}</title>
            </path>
            </OverlayTrigger>
          );
        })}
      </svg>
    </Container>
  );
}

export default SvgMap;
