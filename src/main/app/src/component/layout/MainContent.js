import React from "react";
import MainSection from "./MainSection";
import TopBar from "./TopBar";


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
