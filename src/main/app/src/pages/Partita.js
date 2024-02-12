import React from "react";
import { Button, Container } from "react-bootstrap";
import CreaLobby from "../component/partita/CreaLobby";
import Lobby from "../component/partita/Lobby";

export default class Partita extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      mostraCreaLobby: false,
      mostraLobby: false,
    };
  }

  handleCreaLobby = () => {
    this.setState({ mostraCreaLobby: true });
  };

  handleUniscitiLobby = () => {
    this.setState({ mostraLobby: true });
  };

  render() {
    const { user } = this.props;
    return (
      <div>
        <Container>
          <span className="h3">Benvenuto </span>
          {user ? (
            <span className="h3">{user}</span>
          ) : (
            <span className="h3">Utente Generico</span>
          )}
          <p>Crea una lobby oppure unisciti a una gia esistente</p>
        </Container>

        <Button
          variant="primary"
          className="m-2"
          onClick={this.handleCreaLobby}
        >
          Crea Lobby
        </Button>
        <Button variant="secondary" onClick={this.handleUniscitiLobby}>
          Unisciti a Lobby
        </Button>

        {this.state.mostraCreaLobby && (
          <div>
            <CreaLobby />
          </div>
        )}

        {this.state.mostraLobby && (
          <div>
            <Lobby />
          </div>
        )}
      </div>
    );
  }
}
