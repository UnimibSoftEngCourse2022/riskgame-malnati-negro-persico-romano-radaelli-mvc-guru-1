import React, { useState, useEffect } from "react";
import { Card, Container, Carousel, Button } from "react-bootstrap";
import AppController from "../../application/AppController";
import "../../styles/carouselStyle.css";
import { useNavigate, useParams } from "react-router-dom";

function CarouselComponent() {
  const [index, setIndex] = useState(0);
  const [lobbies, setLobbies] = useState([]);
  const [isLobbyCreated, setIsLobbyCreated] = useState("");
  const navigate = useNavigate();
  const { id } = useParams();

  const uniscitiAllaLobby = (idPartita) => {
    console.log("nickname carousel", id);
    console.log("idPartita carousel", idPartita);

    let effectiveNickname = id;
    if (!effectiveNickname || effectiveNickname === "null") {
      effectiveNickname = `Ospite_${Date.now()}`;
    }
    console.log(effectiveNickname);

    navigate(`/lobby/${idPartita}?nickname=${effectiveNickname}`);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await AppController.getPartite();
        setLobbies(data);
        console.log("Lobby creata con successo!");
      } catch (error) {
        console.error("Error:", error);
        console.log("Creazione lobby fallita. Per favore, riprova.");
      }
    };

    fetchData();
  }, []);

  const handleSelect = (selectedIndex) => {
    setIndex(selectedIndex);
  };

  return (
    <Container className="d-flex justify-content-center">
      {lobbies.length > 0 ? (
        <Carousel activeIndex={index} onSelect={handleSelect} className="w-75">
          {lobbies.map((lobby) => (
            <Carousel.Item key={lobby.id} className="p-2 mb-2">
              <Container className="text-center d-flex justify-content-center mb-4">
                <Card className="w-25 d-inline-block">
                  <Card.Header>
                    <h5 className="card-title">Partita ID: {lobby.id}</h5>
                  </Card.Header>
                  <Card.Body>
                    <p className="card-text">
                      Difficoltà: {lobby.configuration.difficolta}
                    </p>
                    <p className="card-text">
                      Mappa: {lobby.configuration.nomeMappa}
                    </p>
                    <p className="card-text">
                      Giocatori: {lobby.players.length}/
                      {lobby.configuration.players}
                    </p>
                    <Button onClick={() => uniscitiAllaLobby(lobby.id)}>
                      Unisciti alla Lobby
                    </Button>
                  </Card.Body>
                </Card>
              </Container>
            </Carousel.Item>
          ))}
        </Carousel>
      ) : (
        <p>Nessuna partita disponibile</p>
      )}
    </Container>
  );
}

export default CarouselComponent;
