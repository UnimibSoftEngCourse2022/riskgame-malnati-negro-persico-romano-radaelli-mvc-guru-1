import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";

function LobbyPage() {
  const params = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  return <LobbyClass params={params} navigate={navigate} location={location} />;
}
class LobbyClass extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      idPartita: null,
      nickname: null,
      utentiConnessi: [],
    };
  }

  componentDidMount() {
    const { params, location } = this.props;
    const query = new URLSearchParams(location.search);
    const idPartita = params.idPartita;
    const nickname = query.get("nickname");

    console.log(
      `Componente montato. Nickname: ${nickname}, ID Partita: ${idPartita}`
    );

    this.setState({ idPartita, nickname });
    this.connettiALobby(idPartita, nickname);
  }

  connettiALobby(idPartita, nickname) {
    console.log(
      `Tentativo di connessione alla lobby con ID Partita: ${idPartita}`
    );

    AppController.entraInPartita(idPartita, nickname)
      .then((partita) => {
        console.log("Connesso alla partita:", partita);
        const utentiConnessi = partita.players
          .filter((player) => player.userName !== null)
          .map((player) => player.userName);

        // Aggiornare lo stato con l'elenco degli utenti connessi
        this.setState({ utentiConnessi });
        console.log("utentiConnessi:", partita.players.username);
      })
      .catch((error) => {
        console.error("Errore durante la connessione alla lobby:", error);
      });
  }

  render() {
    const { idPartita, utentiConnessi, giocatoriTotali } = this.state;
    console.log("utentiConnessi", utentiConnessi);
    return (
      <div>
        <h2>Lobby: {idPartita}</h2>
        <h3>Utenti Connessi:</h3>
        <div>
          {Array.isArray(utentiConnessi) && utentiConnessi.length > 0 ? (
            utentiConnessi.map((utente, index) => (
              <p className="text-dark" key={index}>
                {utente}
              </p>
            ))
          ) : (
            <p>Nessun utente connesso</p>
          )}
        </div>
      </div>
    );
  }
}

export default LobbyPage;
