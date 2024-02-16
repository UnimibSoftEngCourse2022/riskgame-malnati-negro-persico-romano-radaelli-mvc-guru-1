import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";

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
      utentiTotali: null,
    };

    this.updatePartita = this.updatePartita.bind(this);
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
    // questa classe lobby diventa un listener
    PartitaObserverSingleton.addListener(this);
    // this.connettiALobby(idPartita, nickname);
  }

  updatePartita(partita) {
    console.log("update partita in lobby", partita);
    const utentiConnessi = partita.players
      .filter((player) => player.userName !== null)
      .map((player) => player.userName);
    this.setState({ utentiConnessi });

    const utentiTotali = partita.configuration.players;
    this.setState({ utentiTotali });
  }

  esciDallaLobby = () => {
    const { idPartita, nickname } = this.state;
    AppController.esciDallaPartita(idPartita, nickname);
    // Controlla se il nickname inizia con "Ospite_"
    if (nickname.startsWith("Ospite_")) {
      this.props.navigate(`/partita/null`);
    } else {
      this.props.navigate(`/partita/${nickname}`);
    }
  };

  render() {
    const { idPartita, utentiConnessi, utentiTotali } = this.state;
    console.log("utentiConnessi in Lobby", utentiConnessi);
    return (
      <div>
        <h2>Lobby: {idPartita}</h2>
        <p>utenti totali: {utentiTotali}</p>
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
          <button onClick={this.esciDallaLobby}>Esci dalla Lobby</button>
        </div>
      </div>
    );
  }
}

export default LobbyPage;
