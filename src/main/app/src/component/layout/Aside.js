import React from "react";
import { Navbar, Nav, Row, Col, Container } from "react-bootstrap";

function Aside() {
  return (
    <Row>
      <Col xs={3} sm={3} md={3} lg={2} xl={2} style={{ background: "gray", height: "100vh" }}>
        <Container fluid>
          <Navbar variant="dark" className="flex-column" style={{ width: "100%", height: "100%" }}>
            <Navbar.Brand href="#home">Page</Navbar.Brand>
            <Nav className="flex-column">
              <span className="nav-category">Play</span>
              <Nav.Link href="#play-host">Play as Host</Nav.Link>
              <Nav.Link href="#play-guest">Play as Guest</Nav.Link>
              <span className="nav-category">Learn</span>
              <Nav.Link href="#rules">Rules</Nav.Link>
              <span className="nav-category">Community</span>
              <Nav.Link href="#news">News</Nav.Link>
            </Nav>
          </Navbar>
        </Container>
      </Col>
    </Row>
  );
}

export default Aside;
