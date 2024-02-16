import React, { useState } from "react";
import { useParams } from "react-router-dom";

function SetUpStateMap(props) {
  const { idPlayer } = useParams();
  const [player, setPlayer] = useState([]);
  const [color, setColor] = useState("");
  const [map, setMap] = useState([]);

  setPlayer(props.filter((player) => player.id === idPlayer));

  return (
    <div>
      map component
      <p>sei il giocatore</p>
      <p>{player}</p>
    </div>
  );
}

export default SetUpStateMap;
