import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { Navbar, Nav } from "react-bootstrap";



export default class Home extends React.Component {

    constructor() {
        super();

    }


    render(){



        return (
            <div>
               <h1 className="bg-success">Risiko </h1>
               
               
        <Row style={{ height: "100vh" }} className="m-0">
          <Col
            xs={3}
            sm={3}
            md={3}
            lg={2}
            xl={2}
            style={{ background: "gray" }}
          >
            <Container fluid className="border border-dark m-0 p-0">
              <h1>Page </h1>
            </Container>
            <Navbar
              bg=""
              variant=""
              className="d-flex flex-column align-items-start justify-content-around"
            >
              <ul>
                <span className="nav-category">Play</span>
                <li>
                  <Nav.Link className="">
                    <span className="">Play as Host</span>
                  </Nav.Link>
                </li>
                <li>
                  <Nav.Link className="">
                    <span className="">Play as Guest</span>
                  </Nav.Link>
                </li>
              </ul>
              <ul>
                <span className="nav-category">Learn</span>
                <li>
                  <i className="bi bi-check-circle icon-link"></i>
                  <span className="text-link">Rules</span>
                </li>
              </ul>
              <ul>
                <span className="nav-category">Comunity</span>
                <li>
                  <span className="text-link">News</span>
                </li>
              </ul>
            </Navbar>
          </Col>
          <Col>
            <Container className="">
              <Row>
                <Col xs={3} sm={3} md={6} lg={6} xl={6}>
                  <Container className="m-4">
					<h1>Home </h1>
                  </Container>
                </Col>
                <Col xs={3} sm={3} md={6} lg={6} xl={6}>
                  <h3 className="display-3 mt-4">Login Page</h3>
                  <p className="display-4">Click to Log In</p>
                  <button
                    className="btn btn-success w-25 m-2"
                    
                  >
                    Log In
                  </button>
                  <button
                    className="btn btn-dark w-25 m-2"
                    
                  >
                    Play as Guest
                  </button>
                </Col>
              </Row>
            </Container>
          </Col>
        </Row>
            </div>
        )
    }
}

