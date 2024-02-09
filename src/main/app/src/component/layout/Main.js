import React, { useState } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import Login from "../Login";
import SignUp from "../SignUp";
import { useNavigate } from "react-router-dom"; 
import Istruzioni from './Istruzioni';

function Main() {
  const [renderLogin, setRenderLogin] = useState(false);
  const [renderSignup, setSignup] = useState(false);
  const [renderMain, setMain] = useState(true);
  const [username, setUsername] = useState(null);

  const navigate = useNavigate(); 

  const handlerId = (id) => {
    setUsername(id);
  };

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
    setSignup(false); 
  };
  
  const handlePlay = () => {
    navigate(`/partita/${username}`);
  };
  
  return (
    <Col xs={9} sm={9} md={9} lg={10} xl={10}>
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
                    <Button
                      className="btn btn-dark w-25 m-2"
                      onClick={handleSignup}
                    >
                      Sign Up
                    </Button>
                  </div>
              </Row>
            </Container>
      )}
      {renderLogin && (
        <Container>
          <Login onUsernameSubmit={handlerId} /> 
        </Container>
      )}
      {renderSignup && (
        <Container>
          <SignUp onSignupSuccess={handleLogin} />
        </Container>
      )}
      <Container>
        <Button
          className="btn-success w-25 m-2"
          onClick={handlePlay}
        >
          Gioca Partita
        </Button>
      </Container>
       
       <Row>
		  <Container>
			  <Istruzioni />
		  </Container>
       </Row>
    </Col>
  );
}

export default Main;
