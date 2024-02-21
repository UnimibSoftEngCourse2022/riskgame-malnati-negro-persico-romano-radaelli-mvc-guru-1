import React from "react";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import SvgMap from "./SvgMap";
import Console from "./Console";

function EndTurnState({ game }) {
  console.log("game in EndTurn", game);

  const handleTerritoryClick = (territoryName, event) => {
    console.log("tipo di evento on click", event);

    if (event === "add") {
      console.log("Click sinistro");
    } else if (event === "addNonOwned") {
      console.log("Click destro");
    }
  };

  // qua logica per passare lo spostamento

  return (
    <div style={{ width: "100%", height: "100%" }}>
      <SvgMap onTerritoryClick={handleTerritoryClick} />
      <Console />
    </div>
  );
}

export default EndTurnState;
