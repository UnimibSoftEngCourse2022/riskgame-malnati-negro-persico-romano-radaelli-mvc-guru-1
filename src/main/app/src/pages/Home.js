import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { Navbar, Nav } from "react-bootstrap";
import LayoutHome from "../component/layout/LayoutHome"

export default class Home extends React.Component {
  constructor() {
    super();
  }

  render() {
    return (
      <div>
        <LayoutHome />
      </div>
    );
  }
}
