import React from "react";
import { Button } from "react-bootstrap";
import CreaLobby from "../component/partita/CreaLobby"

export default class Partita extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      mostraLobby: false, 
    };
  }


  handleCreaLobby = () => {
    this.setState({ mostraLobby: true });
  };

  handleUniscitiLobby = () => {
    this.setState({ mostraLobby: true });
  };

  render() {
    return (
      <div>
        <Button variant="primary" onClick={this.handleCreaLobby}>Crea Lobby</Button>
        <Button variant="secondary" onClick={this.handleUniscitiLobby}>Unisciti a Lobby</Button>
        
        {this.state.mostraLobby && (
          <div>
            <CreaLobby/>
          </div>
        )}
      </div>
    );
  }
}
