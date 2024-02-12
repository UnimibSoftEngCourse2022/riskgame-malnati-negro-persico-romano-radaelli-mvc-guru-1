import React, { useState, useEffect } from "react";

function Lobby() {
  const [lobbies, setLobbies] = useState([]);
  const [isLobbyCreated, setIsLobbyCreated] = useState("");
  
  const joinLobby = (lobbyId) => {
    // Qui puoi gestire l'azione di unirsi a una lobby.
    // Ad esempio, potresti reindirizzare l'utente a una pagina di gioco,
    // o aprire un websocket verso il server di gioco, ecc.
    console.log(`Unirsi alla lobby con ID: ${lobbyId}`);
    // Implementa la logica di unione qui.
  };

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
        setLobbies(data);
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
      {lobbies.length > 0 ? (
        lobbies.map((lobby) => (
          <div key={lobby.id} className="card">
            <div className="card-body">
              <h5 className="card-title">Partita ID: {lobby.id}</h5>
              <p className="card-text">Difficoltà: {lobby.configuration.difficolta}</p>
              <p className="card-text">Mappa: {lobby.configuration.nomeMappa}</p>
              <p className="card-text">Giocatori: {lobby.players.length}/{lobby.configuration.players}</p>
              <button onClick={() => joinLobby(lobby.id)}>Unisciti alla Lobby</button>
            </div>
          </div>
        ))
      ) : (
        <p>Non ci sono lobbies disponibili al momento.</p>
      )}
      <span>{isLobbyCreated}</span>
    </div>
  );
}

export default Lobby;
