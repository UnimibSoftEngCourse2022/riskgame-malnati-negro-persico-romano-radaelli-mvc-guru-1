import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

function SetUpStateMap(props) {
  console.log("props", props);
  const { idPlayer } = useParams();
  const [players, setPlayers] = useState(props.players || []);
  const [player, setPlayer] = useState([]);
  const [color, setColor] = useState("");
  const [map, setMap] = useState([]);

  const foundPlayer = players.find((i) => i.userName === idPlayer);
  setPlayer(foundPlayer);

  console.log("player", player.userName);
  return (
    <div>
      map component
      <p>sei il giocatore</p>
      <p>{player.userName}</p>
    </div>
  );
}

export default SetUpStateMap;
