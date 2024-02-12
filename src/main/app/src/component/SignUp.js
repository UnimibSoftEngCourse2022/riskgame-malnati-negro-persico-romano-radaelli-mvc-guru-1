import React, { useEffect, useState } from "react";
import { Button, Form, Card, Container } from "react-bootstrap";
import { useAuth } from "../auth/AuthContext";

function SignUp({ onSuccess, onDismiss }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confermaPassword, setConfermaPassword] = useState("");

  const { signUp, signUpStatus } = useAuth();

  useEffect(() => {
    if (signUpStatus === "Registrazione riuscita con successo!") {
      onSuccess();
    }
  }, [signUpStatus, onSuccess]);

  const handleSubmit = (e) => {
    e.preventDefault();

    try {
      signUp(username, password);
    } catch (error) {
      console.error("Errore durante l'operazione di registrazione:", error);
    }
  };

  return (
    <div className="d-flex justify-content-center my-4">
      <Card className="p-2 w-50">
        <Card.Header className="d-flex aligns-items-center p-0 m-0">
          <Container className="" style={{ padding: "0 0 0 60px" }}>
            <Card.Title>Sign Up</Card.Title>
            <Card.Text>
              Inserisci i tuoi dati per effetturare la registrazione
            </Card.Text>
          </Container>
          <Container className="p-0 m-0" style={{ width: "50px" }}>
            <Button
              variant="outline-secondary"
              className="close-btn"
              onClick={onDismiss}
            >
              X
            </Button>
          </Container>
        </Card.Header>
        <Card.Body className="d-flex flex-column align-items-center justify-content-center">
          <Form className="w-50" onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Control
                className="text-left"
                type="text"
                placeholder="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Control
                type="password"
                placeholder="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Control
                type="password"
                placeholder="conferma password"
                value={confermaPassword}
                onChange={(e) => setConfermaPassword(e.target.value)}
              />
            </Form.Group>
            <Button className="text-center" type="submit">
              Sign Up
            </Button>
            {signUpStatus && <p>{signUpStatus}</p>}
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}

export default SignUp;
