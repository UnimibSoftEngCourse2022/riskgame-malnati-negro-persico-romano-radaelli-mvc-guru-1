import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";

import LayoutHome from "../component/layout/LayoutHome";
import Lobby from "../pages/Lobby";
import NewPartita from "../pages/NewPartita";

function Root() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LayoutHome children={<Home />} />} />
        <Route path={"/partita/:id"} element={<NewPartita />} />
        <Route path="/lobby/:idPartita" element={<Lobby />} />
      </Routes>
    </Router>
  );
}

export default Root;
