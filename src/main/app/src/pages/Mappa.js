import React from "react";
import { useParams, useLocation } from "react-router-dom"; // Importa useParams e useLocation
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";
import SetupStateMap from "../component/mappa/SetupStateMap";

function MappaPage() {
  const { idPartita } = useParams(); // Ottieni l'idPartita dall'URL
  const location = useLocation(); // Accedi allo state di navigazione
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
    console.log("partita", partita);
    console.log("partita.state", partita.state.type);
    if (partita && partita.state.type === "SetupState") {
      console.log("Stp per andare all component");
      return <SetupStateMap giocatori={partita.players} idPlayer={this.state.nickname} />;
    }
    return null;
  }

  render() {
	 const partita = this.state.partita;
    return (
      <div>
        <h1 className="h1">Prova il nuovo Gioco di Risiko</h1>
        <p>Stato partita: {partita.state.type}</p>
        <div>
          <h2>Informazioni Giocatori:</h2>
          {partita.players
            .filter((player) => player.userName !== null)
            .map((player, index) => (
              <div key={index}>
                <p>Nome Utente: {player.userName}</p>
                <p>ID Gioco: {player.gameId}</p>
                <p>Colore: {player.color}</p>
              </div>
            ))}
        </div>
        {this.renderSetUpState()}
      </div>
    );
  }
}

export default MappaPage;
