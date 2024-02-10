import React, { useState } from "react";
import { Button, Form } from "react-bootstrap"; 

function SignUp({onSignupSuccess}){
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confermaPassword, setConfermaPassword] = useState("");
  const [signupStatus, setSignupStatus] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault(); 

	
    fetch("/register",{
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
          throw new Error('Registrazione fallita con stato: ' + response.status); 
          console.log("Registrazione Fallita!");
        }
        console.log("code status", response);
        
      })
      .then((data) => {
        console.log("Success:", data);
        setSignupStatus("Registrazione riuscita!"); 
        console.log("Registrazione riuscita!");
                if (onSignupSuccess) {
          onSignupSuccess(); 
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        setSignupStatus(error.toString()); 
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
        
        <Form.Group className="mb-3">
          <Form.Label>Conferma password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Conferma password"
            value={confermaPassword}
            onChange={(e) => setConfermaPassword(e.target.value)}
          />
        </Form.Group>

        <Button className="text-center" type="submit">
          Sign Up
        </Button>
      </Form>
      <p>{setSignupStatus}</p>
    </div>
  );
}

export default SignUp;