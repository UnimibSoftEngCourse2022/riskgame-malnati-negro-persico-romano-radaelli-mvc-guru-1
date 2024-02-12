import React from "react";
import { Button, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import Istruzioni from "./layout/Istruzioni";

function PlayComponent() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const handlePlay = () => {
    navigate(`/partita/${user}`);
  };

  return (
    <>
      <Container className="p-3 my-4 shadow w-50 border rounded">
        <h3 className="display-4">Gioca una Partita!</h3>
        <p className="">
          Puoi decidere di giocare una partita anche come utente non registrato
        </p>
        <Button
          className="btn-success w-25 m-2 border border-dark shadow"
          onClick={handlePlay}
        >
          Play Game
        </Button>
      </Container>
      <Istruzioni />
    </>
  );
}

export default PlayComponent;
