import React from "react";
import { Navbar, Nav, Col, Container } from "react-bootstrap";

function Aside() {
  return (
    <aside style={{ width: "250px", height: "100vh" }}>
      <Col style={{ background: "gray", height: "100%" }}>
        <Container fluid className="p-4">
          <img alt={"foto-logo"} />
        </Container>
        <Container fluid>
          <Navbar
            variant="dark"
            className="flex-column"
            style={{ width: "100%", height: "100%" }}
          >
            <Navbar.Brand href="#home">
              <Nav.Link href="#Risiko">Risiko</Nav.Link>
            </Navbar.Brand>
            <Nav className="flex-column">
              <Nav.Link href="#Istruzioni">Istruzioni gioco Risiko</Nav.Link>
              <Nav.Link href="#difficolta">Legenda Difficoltà</Nav.Link>
              <Nav.Link href="#novita">Novità del gioco</Nav.Link>
            </Nav>
          </Navbar>
        </Container>
      </Col>
    </aside>
  );
}

export default Aside;
