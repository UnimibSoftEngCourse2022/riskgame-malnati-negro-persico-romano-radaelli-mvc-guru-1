import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { Navbar, Nav } from "react-bootstrap";
import LayoutHome from "../component/layout/LayoutHome"

export default class PartitaRouter extends React.Component {

    

    constructor(props) {
        super(props);
        const paths = window.location.href.split("/");
        this.idPartita = paths[paths.findIndex(el => el === "partita") + 1];

        this.state = {
            nickname: undefined,
            isLogged: false
        }
    }

    handle_set_giocatore = (nick, isImprenditore) => {
        this.setState({nickname: nick, isLogged})
    }

    render() {
		<div>
		<Button>Crea</Button>
		<Button>Unisciti</Button>
		</div>
    }
}