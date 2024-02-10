import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useAuth } from "../auth/AuthContext";

function Login({ onUsernameSubmit }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginStatus, setLoginStatus] = useState("");

  const { login } = useAuth();

  const handleSubmit = (e) => {
    e.preventDefault();

    login(username, password, (successMessage, errorMessage) => {
      if (successMessage) {
        setLoginStatus(successMessage);
      } else if (errorMessage) {
        setLoginStatus(errorMessage);
      }
    });

    onUsernameSubmit(username);
  };

  return (
    <div>
      <Form
        className="d-flex flex-column align-items-center justify-content-center"
        onSubmit={handleSubmit}
      >
        <Form.Group>
          <Form.Label>Inserisci username</Form.Label>
          <Form.Control
            className="text-center"
            type="text"
            placeholder="Inserisci username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Inserisci password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>

        <Button className="text-center" type="submit">
          Log In
        </Button>
      </Form>

      {loginStatus && <p>{loginStatus}</p>}
    </div>
  );
}

export default Login;
