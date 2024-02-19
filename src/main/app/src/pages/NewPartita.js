import React from "react";
import {
  Button,
  Form,
  Card,
  Carousel,
  Container,
  Alert,
} from "react-bootstrap";
import AppController from "../application/AppController";
import { withAuth } from "../auth/AuthContext";
import { useNavigate, useParams } from "react-router-dom";
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
      index: 0,
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

  handleNomeLobby = (e) => {
    this.setState({ nomeMappa: e.target.value });
  };

  handleSubmit = async (e) => {
    e.preventDefault();
    const { difficolta, players, nomeMappa } = this.state;

    if (!difficolta || !players || !nomeMappa) {
      this.setState({
        isLobbyCreated: "Per favore, completa tutti i campi richiesti.",
      });
      return;
    }

    const configuration = {
      difficolta,
      players: parseInt(players),
      nomeMappa,
    };
    try {
      const result = await AppController.creaPartita(configuration);
      this.setState({
        // isLobbyCreated: `Lobby creata con successo! ID Lobby: ${result.id}`,
        isLobbyCreated: `Lobby creata con successo!`,
      });

      this.fetchLobbies();
    } catch (error) {
      this.setState({
        isLobbyCreated: `Errore nella creazione della lobby: ${error.message}`,
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
    try {
      AppController.entraInPartita(idPartita, effectiveNickname);
      this.props.navigate(`/lobby/${idPartita}?nickname=${effectiveNickname}`);
    } catch (error) {
      alert("Errore: " + error.message); // Mostra un popup con l'errore
      console.error("Errore durante l'entrata nella partita:", error);
    }
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
    <p>..caricamento Lobby in corso..</p>;
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
                <p>Nome Lobby: {lobby.configuration.nomeMappa}</p>
                <p>
                  Giocatori: {lobby.players.length}/
                  {lobby.configuration.players}
                </p>
                {/* Implementa qui la funzione per unirsi alla lobby se necessario */}
                <Button
                  onClick={() => {
                    if (lobby.players.length >= lobby.configuration.players) {
                      alert("La lobby è piena!");
                    } else {
                      this.uniscitiAllaLobby(lobby.id);
                    }
                  }}
                >
                  Vai
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
    const { difficolta, nomeMappa, isLobbyCreated } = this.state;
    return (
      <div
        style={{ height: "100vh" }}
        className="container d-flex flex-column justify-content-center align-itmes-center border"
      >
        <Container className="p-2 d-flex justify-content-center">
          <Card className="w-50 p-2 d-flex align-items-center justify-content-center">
            <Card.Header className="w-100">
              Crea la tua Lobby personalizzata
            </Card.Header>
            <Form className="w-75 p-3" onSubmit={this.handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Difficoltà</Form.Label>
                <Form.Control
                  as="select"
                  value={difficolta}
                  onChange={this.handleDifficolta}
                  className="border border-dark"
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
                    className="border border-dark"
                  >
                    <option value="">Seleziona il numero di giocatori</option>
                    {this.renderLobbyOptions()}
                  </Form.Control>
                </Form.Group>
              )}

              <Form.Group className="mb-3">
                <Form.Label>Nome Lobby</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Inserisci il nome della Lobby"
                  value={nomeMappa}
                  onChange={this.handleNomeLobby}
                  className="border border-dark"
                />
              </Form.Group>

              <Button variant="primary" type="submit">
                Crea Lobby
              </Button>
            </Form>
          </Card>
        </Container>
        {isLobbyCreated && (
          <p
            style={{ fontSize: "18px", fontWeight: "bold" }}
            className={
              isLobbyCreated === "Lobby creata con successo!"
                ? "text-success"
                : "text-danger"
            }
          >
            {isLobbyCreated}
          </p>
        )}
        <Container className="w-75 d-flex justify-content-center">
          {this.renderCarousel()}
        </Container>
      </div>
    );
  }
}

export default withAuth(NewPartitaPage);
