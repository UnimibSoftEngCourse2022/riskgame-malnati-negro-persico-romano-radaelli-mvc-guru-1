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

  // connettiALobby(idPartita, nickname) {
  //   console.log(
  //     `Tentativo di connessione alla lobby con ID Partita: ${idPartita}`
  //   );

  //   try {
  //     AppController.entraInPartita(idPartita, nickname);
  //   } catch (error) {
  //     console.error("Errore durante la connessione alla lobby:", error);
  //   }
  // }

  updatePartita(partita) {
    const utentiConnessi = partita.players
      .filter((player) => player.userName !== null)
      .map((player) => player.userName);
    this.setState({ utentiConnessi });
  }

  render() {
    const { idPartita, utentiConnessi } = this.state;
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
