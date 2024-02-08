import React, { useState } from "react";
import { Button, Form } from "react-bootstrap"; 

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault(); 

    fetch("/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Success:", data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };

  return (
    <div>
      <Form className="d-flex flex-column align-items-center justify-content-center" onSubmit={handleSubmit}>
        <Form.Group>
          <Form.Label>Inserisci username</Form.Label>
          <Form.Control
            className="text-center"
            type="text"
            placeholder="inserisci username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Inserisci password</Form.Label>
          <Form.Control
            type="password"
            placeholder="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>
        <Button className="text-center" type="submit">
          Log In
        </Button>
      </Form>
    </div>
  );
}

export default Login;
