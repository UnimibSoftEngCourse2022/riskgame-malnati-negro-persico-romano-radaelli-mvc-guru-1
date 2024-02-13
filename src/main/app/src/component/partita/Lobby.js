import React, { useState, useEffect } from "react";
import AppController from '../../application/AppController';
function Lobby() {
  const [lobbies, setLobbies] = useState([]);
  const [isLobbyCreated, setIsLobbyCreated] = useState("");
  
  const joinLobby = async (lobbyId) => {
    const pathArray = window.location.pathname.split('/');
	const nickname = pathArray[pathArray.length - 1] === 'null' ? `Ospite_${Date.now()}` : pathArray[pathArray.length - 1];
	console.log(nickname);
    try {
      AppController.entraInPartita(lobbyId, nickname);
      console.log(`Unirsi alla lobby con ID: ${lobbyId}`);
      // Qui puoi gestire la navigazione all'UI della partita o altre azioni post-unione
    } catch (error) {
      console.error("Errore durante l'ingresso nella lobby:", error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await AppController.getPartite();
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
