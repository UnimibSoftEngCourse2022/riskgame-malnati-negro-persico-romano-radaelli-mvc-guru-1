import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";
import { Button, Card, Container } from "react-bootstrap";

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
      partita: null,
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
    PartitaObserverSingleton.addListener(this);
  }

  updatePartita(partita) {
    console.log("update partita in lobby", partita);
    this.setState({ partita });
    const utentiConnessi = partita.players
      .filter((player) => player.userName !== null)
      .map((player) => player.userName);
    this.setState({ utentiConnessi });

    const utentiTotali = partita.configuration.players;
    this.setState({ utentiTotali });

    console.log("itenti totali", utentiTotali);
    console.log("utentiConnessi", utentiConnessi);

    if (utentiConnessi.length === utentiTotali) {
      console.log("deento navigazione");
      console.log("deento navigazione", this.state.nickname);
      console.log("deento partita navigazione", partita);
      this.props.navigate(`/mappa/${this.state.nickname}`, {
        state: { partita },
      });
    }
  }

  esciDallaLobby = () => {
    const { idPartita, nickname } = this.state;
    AppController.esciDallaPartita(idPartita, nickname);

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
      <Container>
        <h2>Lobby: {idPartita}</h2>
        <p>utenti totali: {utentiTotali}</p>
        <h3>Utenti Max permessi:</h3>
        <Container>
          {Array.isArray(utentiConnessi) && utentiConnessi.length > 0 ? (
            utentiConnessi.map((utente, index) => (
              <Card className="text-dark w-50" key={index}>
                <Card.Body>{utente}</Card.Body>
              </Card>
            ))
          ) : (
            <p>Al momento non c'è nessun utente connesso</p>
          )}
          <Button onClick={this.esciDallaLobby}>Esci dalla Lobby</Button>
        </Container>
      </Container>
    );
  }
}

export default LobbyPage;
