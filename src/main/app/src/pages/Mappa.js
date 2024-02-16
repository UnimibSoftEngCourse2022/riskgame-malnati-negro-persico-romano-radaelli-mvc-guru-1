import React from "react";
import { useParams, useLocation } from "react-router-dom"; // Importa useParams e useLocation
import PartitaObserverSingleton from "../application/PartitaObserverSingleton";
import SetupStateMap from "../component/mappa/SetupStateMap";

function MappaPage() {
  const { idPartita } = useParams(); // Ottieni l'idPartita dall'URL
  const location = useLocation(); // Accedi allo state di navigazione

  return <Mappa idPartita={idPartita} partita={location.state?.partita} />;
}

class Mappa extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      statoPartita: null,
      giocatori: [],
    };

    this.updatePartita = this.updatePartita.bind(this);
  }

  componentDidMount() {
    PartitaObserverSingleton.addListener(this);

    console.log("props partita", this.props.partita);
    if (this.props.partita) {
      this.updatePartita(this.props.partita);
    }
  }

  updatePartita(partita) {
    console.log("stato partita", partita);
    if (partita && partita.state && partita.players) {
      this.setState({
        statoPartita: partita.state.type,
        giocatori: partita.players,
      });
    }
  }

  render() {
    const { statoPartita, giocatori } = this.state;

    return (
      <div>
        <h1 className="h1">Prova il nuovo Gioco di Risiko</h1>
        <p>Stato partita: {statoPartita}</p>
        {statoPartita === "SetUpState" && (
          <div>Qua componente set up state</div>
        )}
        <div>
          <h2>Informazioni Giocatori:</h2>
          {giocatori
            .filter((player) => player.userName !== null)
            .map((player, index) => (
              <div key={index}>
                <p>Nome Utente: {player.userName}</p>
                <p>ID Gioco: {player.gameId}</p>
                <p>Colore: {player.color}</p>
              </div>
            ))}
        </div>
        {console.log("giocatori", giocatori)}
        <SetupStateMap props={giocatori} />
      </div>
    );
  }
}

export default MappaPage;
