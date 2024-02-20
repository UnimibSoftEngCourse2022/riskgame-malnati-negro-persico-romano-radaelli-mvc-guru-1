import React from "react";
import AppController from "../application/AppController";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";
import { Button, Card, Container } from "react-bootstrap";

import { IoMdPerson } from "react-icons/io";

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
    this.setState({ idPartita, nickname });
    PartitaObserverSingleton.addListener(this);
  }

  updatePartita(partita) {
    this.setState({ partita });
    const utentiConnessi = partita.players
      .filter((player) => player.userName !== null)
      .map((player) => player.userName);
    this.setState({ utentiConnessi });

    const utentiTotali = partita.configuration.players;
    this.setState({ utentiTotali });

    if (utentiConnessi.length === utentiTotali) {
      console.log("Partita che sto passando alla mappa: ", partita);
      this.props.navigate(`/mappa/${this.state.nickname}`, {
        state: { partita },
      });
    }
  }

  esciDallaLobby = () => {
    const { idPartita, nickname } = this.state;
    AppController.esciDallaPartita(idPartita, nickname);
    AppController.unsubscribeToEsitiPartita(idPartita, nickname);

    if (nickname.startsWith("Ospite_")) {
      this.props.navigate(`/partita/null`);
    } else {
      this.props.navigate(`/partita/${nickname}`);
    }
  };

  render() {
    const { idPartita, utentiConnessi, utentiTotali, partita } = this.state;
    console.log("partita il Lobby", partita);
    return (
      <Container className="p-2">
        <h2>Ti sei unito alla Lobby: {partita?.configuration.nomeMappa}</h2>
        <p>utenti necessari a far inziare la partita: {utentiTotali}</p>
        <h3>Utenti collegati:</h3>
        <Container className="w-50 bg-secondary d-flex flex-row border rounded shadow">
          {Array.isArray(utentiConnessi) && utentiConnessi.length > 0 ? (
            utentiConnessi.map((utente, index) => (
              <Card className="text-dark w-25 m-2" key={index}>
                <Card.Header>
                  <IoMdPerson style={{ color: "#000000", fontSize: "60px" }} />
                </Card.Header>
                <Card.Body>{utente}</Card.Body>
              </Card>
            ))
          ) : (
            <p>Al momento non c'è nessun utente connesso</p>
          )}
        </Container>
        <Button className="mt-3" onClick={this.esciDallaLobby}>
          Esci dalla Lobby
        </Button>
      </Container>
    );
  }
}

export default LobbyPage;
