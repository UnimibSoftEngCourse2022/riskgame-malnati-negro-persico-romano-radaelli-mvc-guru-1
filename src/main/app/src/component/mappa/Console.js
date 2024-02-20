import { useState, useEffect } from "react";
import { Container, Button, Nav, Navbar, Modal } from "react-bootstrap";
import { GiInvertedDice3 } from "react-icons/gi";
import PartitaObserverSingleton from "../../application/PartitaObserverSingleton";

function Console({ carriTerritorio, territoryAttack, territoryDefense }) {
  const [dadiSelezionati, setDadiSelezionati] = useState(0);
  const [esiti, setEsiti] = useState({});
  const [showEsiti, setShowEsiti] = useState(false);

  const handleDiceClick = (numDadi) => {
    setDadiSelezionati((prevDadiSelezionati) => prevDadiSelezionati + numDadi);
  };

  const handleClose = () => setShowEsiti(false);

  useEffect(() => {
    function updateEsiti(esiti) {
      console.log("Esiti in console: ", esiti);
      setEsiti(esiti);
      setShowEsiti(true);
    }

    PartitaObserverSingleton.addListenerEsiti(updateEsiti);

    return () => {
      PartitaObserverSingleton.removeListenerEsiti(updateEsiti);
    };
  }, []);

  // const handleShowAlert = () => setShowAlert(true);

  return (
    <Container className="bg-secondary border rounded shadow">
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
            <button
              className="border rounded border-danger"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded border-danger"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
            <button
              className="border rounded border-danger"
              onClick={() => handleDiceClick(1)}
            >
              <GiInvertedDice3 style={{ color: "red", fontSize: "60px" }} />
            </button>
          </>
        )}
      </Container>
      <Container>
        <div>
          <span> attacchi con</span>
          {dadiSelezionati && <span> {dadiSelezionati}</span>}
          <span>dadi</span>
        </div>

        {territoryAttack && territoryDefense && (
          <div>
            <span>{territoryAttack}</span>
            <span> attacca </span>
            <span>{territoryDefense}</span>
            <p>Sei sicuro di voler lanciare l'attacco ?</p>
            <Button className="bg-danger">Attacca</Button>
          </div>
        )}
      </Container>

      {/* <Modal show={showEsiti} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Esito dell'Attacco</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {esiti.isConquered ? (
            <p>Il territorio è stato conquistato.</p>
          ) : (
            <p>Il territorio non è stato conquistato.</p>
          )}
          <p>Truppe perse dall'attaccante: {esiti.lostAttTroops}</p>
          <p>Truppe perse dal difensore: {esiti.lostDefTroops}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Chiudi
          </Button>
        </Modal.Footer>
      </Modal> */}
    </Container>
  );
}
export default Console;
