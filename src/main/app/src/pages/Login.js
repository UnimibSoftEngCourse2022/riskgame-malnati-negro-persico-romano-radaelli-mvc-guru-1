import React from "react";
import { Container, Button, Form } from "react-bootstrap";

export default class Login extends React.Component {
  constructor() {
    super();
    this.state = {
      username: "",
      password: "",
    };
  }

  setUsername = (e) => {
    this.setState({ username: e.target.value });
  };

  setPassword = (e) => {
    this.setState({ password: e.target.value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const { username, password } = this.state;
	console.log("username" , username);
	console.log("password" , password);

    fetch('/login', { 
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
    .then(response => response.json())
    .then(data => {
      console.log('Success:', data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  };
  render() {
    return (
      <Container className="d-flex justify-content-center align-items-center border border-danger">
        <h1 className="display-3 text-center">Login Page</h1>
        <Form className="d-flex flex-column align-items-center justify-content-center" onSubmit={this.handleSubmit}>
          <Form.Group>
            <Form.Label>Inserisci username</Form.Label>
            <Form.Control
              className="text-center"
              type="text"
              placeholder="inserisci username"
              value={this.state.username}
              onChange={this.setUsername}
            ></Form.Control>
          </Form.Group>
          
          <Form.Group className="mb-3">
            <Form.Label>Inserisci password</Form.Label>
            <Form.Control
              type="password" // Cambiato da text a password per nascondere l'input
              placeholder="password"
              value={this.state.password}
              onChange={this.setPassword}
            />
          </Form.Group>
          <Button className="text-center" type="submit">
            Log In
          </Button>
        </Form>
      </Container>
    );
  }
}
