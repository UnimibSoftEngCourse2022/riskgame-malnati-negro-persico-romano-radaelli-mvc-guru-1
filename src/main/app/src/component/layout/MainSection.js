import React from "react";
import { Container } from "react-bootstrap";

function MainSection({ children }) {
  return (
    <Container className="d-flex justify-content-center py-2">
      {children}
    </Container>
  );
}

export default MainSection;
