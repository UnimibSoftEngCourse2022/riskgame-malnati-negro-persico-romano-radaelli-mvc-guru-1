import React from "react";
import MainContent from "./MainContent";

const LayoutMappa = ({ children }) => {
  return (
    <main className="d-flex">
      <MainContent>{children}</MainContent>
    </main>
  );
};

export default LayoutMappa;
