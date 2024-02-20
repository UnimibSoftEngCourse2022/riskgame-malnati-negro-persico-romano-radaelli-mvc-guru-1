import React from "react";
import Aside from "../layout/Aside";
import MainContent from "./MainContent";

const LayoutMappa = ({ children }) => {
  return (
    <main className="d-flex">
      <MainContent>{children}</MainContent>
    </main>
  );
};

export default LayoutMappa;
