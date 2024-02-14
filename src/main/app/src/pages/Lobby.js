import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";

function LobbyPage() {
  const params = useParams(); // Ottiene i parametri dell'URL, come `idPartita`
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
    const nickname = params.nickname;
    console.log("utente in lobby", nickname);

    this.setState({ idPartita, nickname });

    this.connettiALobby(idPartita);
  }

  connettiALobby(idPartita) {
    AppController.entraInPartita(idPartita, this.state.nickname)
      .then((partita) => {
        this.setState({ utentiConnessi: partita.utentiConnessi });
      })
      .catch((error) =>
        console.error("Errore durante la connessione alla lobby:", error)
      );
  }

  render() {
    const { idPartita, utentiConnessi } = this.state;
    return (
      <div>
        <h2>Lobby: {idPartita}</h2>
        <h3>Utenti Connessi:</h3>
        <ul>
          {Array.isArray(utentiConnessi) ? (
            utentiConnessi.map((utente, index) => (
              <li key={index}>{utente.username}</li>
            ))
          ) : (
            <li>Nessun utente connesso</li>
          )}
        </ul>
      </div>
    );
  }
}

export default LobbyPage;
