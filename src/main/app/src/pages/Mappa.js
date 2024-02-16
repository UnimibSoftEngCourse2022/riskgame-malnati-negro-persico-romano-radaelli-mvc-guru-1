import React from "react";

export default class Mappa extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      statoPartita: null,
      giocatori: [],
    };

    this.updatePartita = this.updatePartita.bind(this);
  }

  componentDidMount() {
    PartitaObserverSingleton.addListener(this.updatePartita);
  }

  updatePartita(partita) {
    console.log("stato partita", partita.state);
    this.setState({ statoPartita: partita.state.type });
    console.log("giocatore partita", partita.players);
    const giocatori = partita.players;
    this.setState({ giocatori });
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
          {giocatori.map((player, index) => (
            <div key={index}>
              <p>Nome Utente: {player.userName}</p>
              <p>ID Gioco: {player.gameId}</p>
              <p>Colore: {player.color}</p>
            </div>
          ))}
        </div>
      </div>
    );
  }
}
