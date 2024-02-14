import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";

// Componente funzione wrapper per passare i hook come props
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
    const { match, location } = this.props;
    const query = new URLSearchParams(location.search);
    const idPartita = match.params.idPartita;
    const nickname = query.get("nickname");

    this.setState({ idPartita, nickname });

    this.connettiALobby(idPartita);
  }

  connettiALobby(idPartita) {
    AppController.entraInPartita(idPartita, this.state.nickname)
      .then((partita) => {
        // Supponiamo che `partita` includa l'elenco aggiornato degli utenti connessi
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
          {utentiConnessi.map((utente, index) => (
            <li key={index}>{utente.username}</li>
          ))}
        </ul>
      </div>
    );
  }
}

export default LobbyPage;
