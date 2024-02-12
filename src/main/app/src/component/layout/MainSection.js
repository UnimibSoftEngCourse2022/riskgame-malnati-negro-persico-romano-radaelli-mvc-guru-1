import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";

function MainSection({ children }) {
  return (
    <Container className="d-flex justify-content-center py-4">
      {children}
    </Container>
  );
}

export default MainSection;
