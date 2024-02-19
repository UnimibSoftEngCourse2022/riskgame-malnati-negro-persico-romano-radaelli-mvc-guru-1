import React from "react";
import { useParams, useLocation } from "react-router-dom";
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";
import SetupStateMap from "../component/mappa/SetupStateMap";
import StartTurnState from "../component/mappa/StartTurnState";
import { Container } from "react-bootstrap";

function MappaPage() {
  const { idPartita } = useParams();
  const location = useLocation();
  console.log("location partita", location.state?.partita);

  return <Mappa idPartita={idPartita} partita={location.state?.partita} />;
}

class Mappa extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      partita: props.partita,
      nickname: null,
    };

    this.updatePartita = this.updatePartita.bind(this);
  }

  componentDidMount() {
    const url = window.location.href;
    const nickname = url.split("/").pop();
    this.setState({ nickname: nickname });
    PartitaObserverSingleton.addListener(this);
  }

  updatePartita(partita) {
    console.log("Partita update", partita);
    if (partita) {
      this.setState({
        partita: partita,
      });
    }
  }

  renderSetUpState() {
    const partita = this.state.partita;

    if (partita && partita.state.type === "SetupState") {
      console.log("Sto per andare all component SetupState");
      return (
        <SetupStateMap
          giocatori={partita.players}
          idPlayer={this.state.nickname}
          game={partita}
        />
      );
    }
    return null;
  }

  renderStartTurnState() {
    const partita = this.state.partita;
    console.log("partita", partita);
    console.log("partita.state", partita.state.type);

    if (partita && partita.state.type === "PlayTurnState") {
      console.log("Sto per andare all component PlayTurnState");
      return <StartTurnState idPlayer={this.state.nickname} game={partita} />;
    }
    return null;
  }

  render() {
    const partita = this.state.partita;
    const nickname = this.state.nickname;
    console.log("stato partita in Mappa page", this.state.partita.state.type);
    console.log("nickname", nickname);

    const currentPlayer = partita.players.find((p) => p.userName === nickname);

    console.log(
      " giocatore corrente",
      currentPlayer ? currentPlayer.userName : "not found"
    );

    const colorPlayer = currentPlayer ? currentPlayer.color : "red";
    console.log("color giocatore", colorPlayer);
    return (
      <Container style={{ height: "100vh" }}>
        <p>Stato partita: {partita.state.type}</p>
        <p style={{ color: colorPlayer }}>
          Sei il giocatore: {this.state.nickname}
        </p>
        <div className="d-flex flew-row justify-content-center align-items-center">
          <span>Il colore delle tue truppe è il: </span>
          <div
            style={{
              backgroundColor: colorPlayer,
              width: "15px",
              height: "15px",
            }}
          ></div>
        </div>

        <Container>
          {this.state.partita.state.type === "SetupState" &&
            currentPlayer?.setUpCompleted === false &&
            this.renderSetUpState()}
        </Container>

        {this.state.partita.state.type === "SetupState" &&
          currentPlayer?.setUpCompleted === true && (
            <div className="card">
              <p className="text-body-secondary">..attendi altri giocatori..</p>
            </div>
          )}

        {this.state.partita.state.type === "StartTurnState" &&
          this.renderStartTurnState()}
      </Container>
    );
  }
}

export default MappaPage;
