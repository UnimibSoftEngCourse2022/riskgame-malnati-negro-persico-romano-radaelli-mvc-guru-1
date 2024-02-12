import React from "react";
import Aside from "../layout/Aside";
import MainContent from "./MainContent";

const LayoutHome = ({ children }) => {
  return (
    <main className="d-flex">
      <Aside />
      <MainContent>{children}</MainContent>
    </main>
  );
};

export default LayoutHome;
