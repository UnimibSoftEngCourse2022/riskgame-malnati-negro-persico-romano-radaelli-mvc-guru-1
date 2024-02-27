import React from "react";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

function EndGameState() {
  const { navigate } = useNavigate();
  return (
    <div style={{ width: "100%", height: "100%" }}>
      <h3>Gioco Terminato</h3>
      <p>torna alla Home</p>
      <Button onClick={() => navigate("/")}>Home</Button>
    </div>
  );
}

export default EndGameState;
