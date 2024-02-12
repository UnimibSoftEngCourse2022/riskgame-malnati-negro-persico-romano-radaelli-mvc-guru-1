import React from "react";
import { Container, Button } from "react-bootstrap";

function LoginOrSignup({ loginHandle, signupHandler }) {
  return (
    <Container className=" w-75 p-4">
      <p className="display-8">
        Registrati e accedi per poter creare mappe personalizzate
      </p>

      <div className="d-flex justify-content-center">
        <Button className="btn-success w-25 m-2 shadow" onClick={loginHandle}>
          Log In
        </Button>
        <Button
          className="btn btn-dark w-25 m-2 shadow"
          onClick={signupHandler}
        >
          Sign Up
        </Button>
      </div>
    </Container>
  );
}

export default LoginOrSignup;
