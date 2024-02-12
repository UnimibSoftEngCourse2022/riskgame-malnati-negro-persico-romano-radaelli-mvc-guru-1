import React, { useState, useEffect } from "react";
import { Button, Form, Container } from "react-bootstrap";

function Lobby() {
  const [data, setData] = useState(null);
  const [isLobbyCreated, setIsLobbyCreated] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch("/partita", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(
            `Creazione lobby fallita con stato: ${response.status}`
          );
        }

        const data = await response.json();
        console.log("Success:", data);
        setData(data);
        setIsLobbyCreated("Lobby creata con successo!");
      } catch (error) {
        console.error("Error:", error);
        setIsLobbyCreated("Creazione lobby fallita. Per favore, riprova.");
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      <h1 className="display-3 text-center">Benvenuto in Lobby</h1>
      <span>{data}</span>
      <span>{isLobbyCreated}</span>
    </div>
  );
}

export default Lobby;
