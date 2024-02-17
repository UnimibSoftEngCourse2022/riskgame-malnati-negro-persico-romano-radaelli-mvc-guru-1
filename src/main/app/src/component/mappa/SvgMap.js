import React, { useEffect, useState } from "react";
import { Container, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";

function SvgMap({ paths, gioco, onTerritoryClick, truppeAssegnate }) {
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
    truppeAssegnate.map;
    // Debugging logs
    console.log("ID del giocatore corrente:", nickname);
    console.log(
      "Giocatori nella partita:",
      gioco.players.map((p) => p.userName)
    );
  }, [gioco, nickname]);

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
            <path
              key={index}
              id={name}
              d={d}
              stroke="black"
              fill={fillColor} // Usa il colore specifico del giocatore
              opacity="0.5"
              onClick={() => isTerritoryOwned && onTerritoryClick(name)}
            >
              <title>{`${name}: ${truppe} `}</title>
            </path>
          );
        })}
      </svg>
    </Container>
  );
}

export default SvgMap;
