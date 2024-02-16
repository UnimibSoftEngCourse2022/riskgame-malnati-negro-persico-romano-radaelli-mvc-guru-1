import React from "react";
import { Button, Container, Form, Card, Carousel } from "react-bootstrap";
import AppController from "../application/AppController";
import { withAuth } from "../auth/AuthContext";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import "../styles/carouselStyle.css";

function NewPartitaPage() {
  const params = useParams();
  const navigate = useNavigate();

  return <NewPartita params={params} navigate={navigate} />;
}

class NewPartita extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      difficolta: "",
      players: "",
      nomeMappa: "",
      isLobbyCreated: "",
      lobbies: [],
      index: 0, // Utilizzato per la gestione dell'indice attivo nel Carousel
    };
  }

  componentDidMount() {
    this.fetchLobbies();
  }

  fetchLobbies = async () => {
    try {
      const data = await AppController.getPartite();
      console.log("data partita", data);
      this.setState({ lobbies: data });
    } catch (error) {
      console.error("Error:", error);
    }
  };

  handleDifficolta = (e) => {
    this.setState({ difficolta: e.target.value });
  };

  handlePlayersNumber = (e) => {
    this.setState({ players: e.target.value });
  };

  handleNomeMappa = (e) => {
    this.setState({ nomeMappa: e.target.value });
  };

  handleSubmit = async (e) => {
    e.preventDefault();
    const { difficolta, players, nomeMappa } = this.state;
    const configuration = {
      difficolta,
      players: parseInt(players),
      nomeMappa,
    };
    try {
      const result = await AppController.creaPartita(configuration);
      this.setState({
        isLobbyCreated: "Lobby creata con successo! ID Lobby: `${result.id}`",
      });
      // Aggiorna il numero totale delle lobby dopo averne creata una nuova
      this.fetchLobbies();
    } catch (error) {
      this.setState({
        isLobbyCreated: "Errore nella creazione della lobby: ${error.message}",
      });
    }
  };

  handleSelect = (selectedIndex) => {
    this.setState({ index: selectedIndex });
  };

  uniscitiAllaLobby = (idPartita) => {
    let effectiveNickname = this.props.params.id;
    if (!effectiveNickname || effectiveNickname === "null") {
      effectiveNickname = `Ospite_${Date.now()}`;
    }
    console.log("effectiveNickname", effectiveNickname);
    AppController.entraInPartita(idPartita, effectiveNickname);
    this.props.navigate(`/lobby/${idPartita}?nickname=${effectiveNickname}`);
  };

  renderLobbyOptions = () => {
    const { difficolta } = this.state;
    switch (difficolta) {
      case "EASY":
        return [2, 3, 4].map((num) => (
          <option key={num} value={num}>
            {num}
          </option>
        ));
      case "MEDIUM":
      case "HARD":
        return [2, 3, 4, 5, 6].map((num) => (
          <option key={num} value={num}>
            {num}
          </option>
        ));
      default:
        return [];
    }
  };

  renderCarousel = () => {
    const { lobbies, index } = this.state;

    return lobbies.length > 0 ? (
      <Carousel
        activeIndex={index}
        onSelect={this.handleSelect}
        className="w-75"
      >
        {lobbies.map((lobby) => (
          <Carousel.Item key={lobby.id} className="p-2 mb-2">
            <Card className="w-100 d-inline-block">
              <Card.Header>
                <h5>Partita ID: {lobby.id}</h5>
              </Card.Header>
              <Card.Body>
                <p>Difficoltà: {lobby.configuration.difficolta}</p>
                <p>Mappa: {lobby.configuration.nomeMappa}</p>
                <p>
                  Giocatori: {lobby.players.length}/
                  {lobby.configuration.players}
                </p>
                {/* Implementa qui la funzione per unirsi alla lobby se necessario */}
                <Button onClick={() => this.uniscitiAllaLobby(lobby.id)}>
                  Unisci a lobby
                </Button>
              </Card.Body>
            </Card>
          </Carousel.Item>
        ))}
      </Carousel>
    ) : (
      <p>Nessuna partita disponibile</p>
    );
  };

  render() {
    // const { user } = this.props;
    const { difficolta, nomeMappa, isLobbyCreated } = this.state;
    return (
      <div>
        {/* <Container>
          <span className="h3">
            Benvenuto nella pagina partita,{" "}
            {user.user ? user.user : "Utente Generico"}
          </span>
          <p>Crea una lobby oppure unisciti a una già esistente</p>
        </Container> */}

        {/* Form per creare una partita */}
        <Form onSubmit={this.handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Difficoltà</Form.Label>
            <Form.Control
              as="select"
              value={difficolta}
              onChange={this.handleDifficolta}
            >
              <option value="">Seleziona un livello di difficoltà</option>
              <option value="EASY">Facile</option>
              <option value="MEDIUM">Medio</option>
              <option value="HARD">Difficile</option>
            </Form.Control>
          </Form.Group>

          {difficolta && (
            <Form.Group className="mb-3">
              <Form.Label>Numero di Giocatori</Form.Label>
              <Form.Control
                as="select"
                value={this.state.players}
                onChange={this.handlePlayersNumber}
              >
                <option value="">Seleziona il numero di giocatori</option>
                {this.renderLobbyOptions()}
              </Form.Control>
            </Form.Group>
          )}

          <Form.Group className="mb-3">
            <Form.Label>Nome Mappa</Form.Label>
            <Form.Control
              type="text"
              placeholder="Inserisci il nome della mappa"
              value={nomeMappa}
              onChange={this.handleNomeMappa}
            />
          </Form.Group>

          <Button variant="primary" type="submit">
            Crea Lobby
          </Button>
        </Form>

        {isLobbyCreated && <p>{isLobbyCreated}</p>}

        {this.renderCarousel()}
      </div>
    );
  }
}

export default withAuth(NewPartitaPage);
