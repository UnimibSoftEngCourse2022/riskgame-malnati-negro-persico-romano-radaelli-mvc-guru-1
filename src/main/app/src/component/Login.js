import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

function Login({ onUsernameSubmit }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginStatus, setLoginStatus] = useState(""); 

  const handleSubmit = (e) => {
    e.preventDefault();


    if (typeof onUsernameSubmit === 'function') {
      onUsernameSubmit(username);
    }

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
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Login fallito con stato: ${response.status}`);
        }
		console.log("response status:", response);
      })
      .then((data) => {
        console.log("Success:", data);
        setLoginStatus("Login riuscito! Benvenuto.");
      })
      .catch((error) => {
        console.error("Error:", error);
        setLoginStatus("Login fallito. Per favore, riprova.");
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
