import React, { useState } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import Login from "../Login";
import SignUp from "../SignUp";
import { Navigate } from "react-router-dom";
import Istruzioni from './Istruzioni';
import TopBar from './TopBar';

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
  
  const handlePlayMatch = () => {
    console.log("handlePlayMatch");

  };
  
  return (
    <Col xs={9} sm={9} md={9} lg={10} xl={10}>
	 <TopBar/>
		
      {renderMain && (
	
            <Container>
              <Row>
                  <Container className="m-4">
                    <h1>Logo</h1>
                  </Container>
                
                  
                  <p className="display-4">Click to Log In or Sign Up</p>
                  <div className="d-flex justify-content-center">
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
                  </div>
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
      <Container>
        <button
        className="btn btn-success w-25 m-2"
                onClick={handlePlayMatch}
              >
                Gioca Partita
              </button>
              </Container>
       
       <Row>
	  <Container>
		  <Istruzioni/>
	  </Container>
       </Row>
    </Col>
    
  );
}

export default Main;
