import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import Aside from "../layout/Aside";
import Main from "../layout/Main";

const LayoutHome = () => {
  return (
    <div>
	    <Col><Aside /></Col>
	    <Col><Main /></Col>
    </div>
  );
};

export default LayoutHome;
