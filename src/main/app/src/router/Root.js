import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";

import LayoutHome from "../component/layout/LayoutHome";
import Lobby from "../pages/Lobby";
import NewPartita from "../pages/NewPartita";
import Partita from "../pages/Partita";
import Mappa from "../pages/Mappa";

function Root() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LayoutHome children={<Home />} />} />
        {/* <Route path={"/partita/:id"} element={<Partita />} /> */}
        <Route path={"/partita/:id"} element={<NewPartita />} />
        <Route path="/lobby/:idPartita" element={<Lobby />} />
        <Route path="/mappa/:id" element={<Mappa />} />
      </Routes>
    </Router>
  );
}

export default Root;
