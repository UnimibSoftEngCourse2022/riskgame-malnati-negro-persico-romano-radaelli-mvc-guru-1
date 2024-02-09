import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import Aside from "../layout/Aside";
import Main from "../layout/Main";

const LayoutHome = () => {
  return (
	  <Row>
	    <Aside />
	    <Main />
    </Row>
  );
};

export default LayoutHome;
