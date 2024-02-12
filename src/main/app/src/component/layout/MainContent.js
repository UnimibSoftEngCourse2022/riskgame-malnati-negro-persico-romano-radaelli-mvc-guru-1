import React, { useState, useEffect } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import Login from "../Login";
import SignUp from "../SignUp";
import { useNavigate } from "react-router-dom";
import Istruzioni from "./Istruzioni";
import MainSection from "./MainSection";
import TopBar from "./TopBar";
import { useAuth } from "../../auth/AuthContext";

function MainContent({ children }) {
  return (
    <div
      className="flex-column w-100"
      style={{ overflowY: "auto", height: "100vh" }}
    >
      <TopBar />
      <MainSection>{children}</MainSection>
    </div>
  );
}

export default MainContent;
