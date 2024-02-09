import React from "react";
import { Button } from "react-bootstrap";
import CreaLobby from "../component/partita/CreaLobby"
import Lobby from "../component/partita/Lobby"

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
    return (
      <div>
        <Button variant="primary" onClick={this.handleCreaLobby}>Crea Lobby</Button>
        <Button variant="secondary" onClick={this.handleUniscitiLobby}>Unisciti a Lobby</Button>
        
        {this.state.mostraCreaLobby && (
          <div>
            <CreaLobby/>
          </div>
        )}
        
                {this.state.mostraLobby && (
          <div>
            <Lobby/>
          </div>
        )}

      </div>
    );
  }
}
