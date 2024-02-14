import React from "react";
import { Button, Container } from "react-bootstrap";
import CreaPartita from "../component/partita/CreaPartita";

import { withAuth } from "../auth/AuthContext";
import CarouselComponent from "../component/partita/CarouselComponent";

class Partita extends React.Component {
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

  render() {
    const { user } = this.props;
    console.log("user in partita", user);

    return (
      <div>
        <Container>
          <span className="h3">Benvenuto nella pagina partita </span>
          {user.user ? (
            <span className="h3">{user.user}</span>
          ) : (
            <span className="h3">Utente Generico</span>
          )}
          <p>Crea una lobby oppure unisciti a una gia esistente</p>
        </Container>
        <CarouselComponent />

        <Button
          variant="primary"
          className="m-2"
          onClick={this.handleCreaLobby}
        >
          Crea Lobby
        </Button>

        {this.state.mostraCreaLobby && (
          <div>
            <CreaPartita />
          </div>
        )}
      </div>
    );
  }
}

export default withAuth(Partita);
