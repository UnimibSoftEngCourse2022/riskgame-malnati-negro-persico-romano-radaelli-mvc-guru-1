
import { useState, useEffect } from "react";
import { Container, Button, Nav, Navbar, Modal } from "react-bootstrap";
import { GiInvertedDice3 } from "react-icons/gi";
import PartitaObserverSingleton from "../../application/PartitaObserverSingleton";

function Console({ carriTerritorio }) {
  const [dadiSelezionati, setDadiSelezionati] = useState(0);
  const [esiti, setEsiti] = useState({}); // Aggiungi uno stato per gli esiti
  const [showEsiti, setShowEsiti] = useState(false);


  const handleDiceClick = (numDadi) => {
    setDadiSelezionati((prevDadiSelezionati) => prevDadiSelezionati + numDadi);
  };
  
  const handleClose = () => setShowEsiti(false);
  
  useEffect(() => {
	  
    const updateEsiti = (nuoviEsiti) => {
		console.log("Esiti in console: ", nuoviEsiti);
      setEsiti(nuoviEsiti);
      setShowEsiti(true); // Aggiorna lo stato con i nuovi esiti
    };

    PartitaObserverSingleton.addListenerEsiti(updateEsiti); // Registra il listener

    return () => {
      PartitaObserverSingleton.removeListenerEsiti(updateEsiti); // Rimuovi il listener al dismount
    };
  }, []);

  return (
    <Container className="bg-secondary border rounded shadow">
      <p>console di gioco</p>
      <Container fluid>
        <Navbar
          variant="dark"
          className="flex-row"
          style={{ width: "100%", height: "100%" }}
        >
          <Navbar.Brand>
            <Button variant="success">Mostra obbiettivo</Button>
          </Navbar.Brand>
          <Nav className="flex-column">
            <Button variant="primary">Mostra carte</Button>{" "}
          </Nav>
        </Navbar>
      </Container>
      <Container>
        {carriTerritorio && carriTerritorio === 1 && (
          <button className="border rounded" onClick={() => handleDiceClick(1)}>
            <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
          </button>
        )}
        {carriTerritorio && carriTerritorio === 2 && (
          <>
            <button
              className="border rounded"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded bg-danger"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
          </>
        )}

        {carriTerritorio && carriTerritorio > 3 && (
          <>
            <button className="" onClick={() => handleDiceClick(1)}>
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button className="" onClick={() => handleDiceClick(1)}>
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
          </>
        )}
      </Container>
      <Container>
        <span> attacchi con</span>
        <span>{dadiSelezionati}</span>
        <span>dadi</span>
      </Container>
      <Button className="bg-danger">Attacca</Button>
      <Button className="bg-primary">Difendi</Button>
      <p>dadi attaccante</p>
       <Modal show={showEsiti} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Esito dell'Attacco</Modal.Title>
        </Modal.Header>
        <Modal.Body>
         {esiti.isConquered ? <p>Il territorio è stato conquistato.</p> : <p>Il territorio non è stato conquistato.</p>}
          <p>Truppe perse dall'attaccante: {esiti.lostAttTroops}</p>
          <p>Truppe perse dal difensore: {esiti.lostDefTroops}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Chiudi
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}
export default Console;
