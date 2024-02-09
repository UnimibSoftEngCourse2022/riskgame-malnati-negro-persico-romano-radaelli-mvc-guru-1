import React, { useState } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import Login from "../Login";
import SignUp from "../SignUp";

function Main() {
  const [renderLogin, setRenderLogin] = useState(false);
  const [renderSignup, setSignup] = useState(false);
  const [renderMain, setMain] = useState(true);

  const handleLogin = () => {
    setRenderLogin(true);
    setMain(false);
    setSignup(false);
  };

  const handleSignup = () => {
    setRenderLogin(false);
    setMain(false);
    setSignup(true);
  };

  const handleMain = () => {
    setRenderLogin(false);
    setMain(true);
    setSignup(true);
  };

  return (
    <Col>
      {renderMain && (

            <Container>
              <Row>
                <Col xs={3} sm={3} md={6} lg={6} xl={6}>
                  <Container className="m-4">
                    <h1>Home</h1>
                  </Container>
                </Col>
                <Col xs={3} sm={3} md={6} lg={6} xl={6}>
                  <h3 className="display-3 mt-4">Login Page</h3>
                  <p className="display-4">Click to Log In</p>
                  <Button
                    className="btn-success w-25 m-2"
                    onClick={handleLogin}
                  >
                    Log In
                  </Button>
                  <button
                    className="btn btn-dark w-25 m-2"
                    onClick={handleSignup}
                  >
                    Sign Up
                  </button>
                </Col>
              </Row>
            </Container>
      )}
      {renderLogin && (
        <Container>
          <Login />
        </Container>
      )}
      {renderSignup && (
        <Container>
          <SignUp />
        </Container>
      )}
    </Col>
  );
}

export default Main;
