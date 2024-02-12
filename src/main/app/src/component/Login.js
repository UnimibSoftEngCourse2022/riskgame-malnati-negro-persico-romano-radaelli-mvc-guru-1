import React, { useState } from "react";
import { Button, Form, Card, Container } from "react-bootstrap";
import { useAuth } from "../auth/AuthContext";

function Login({ onDismiss }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const { login } = useAuth();
  const { signUpStatus } = useAuth();
  const { loginStatus } = useAuth();

  const handleSubmit = (e) => {
    e.preventDefault();

    login(username, password, (successMessage, errorMessage) => {});
  };

  return (
    <div className="d-flex justify-content-center my-4">
      <Card className="p-2 w-50">
        <Card.Header className="d-flex aligns-items-center p-0 m-0">
          <Container className="" style={{ padding: "0 0 0 60px" }}>
            <Card.Title>Log In</Card.Title>

            {signUpStatus && (
              <Card.Text className="mb-2">{signUpStatus}</Card.Text>
            )}
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

        <Form
          className="d-flex flex-column align-items-center justify-content-center mt-4 mb-4"
          onSubmit={handleSubmit}
        >
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
              className="text-left"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </Form.Group>

          <Button className="text-center w-25" type="submit">
            Log In
          </Button>

          {loginStatus && <span className="mt-1">{loginStatus}</span>}
        </Form>
      </Card>
    </div>
  );
}

export default Login;
