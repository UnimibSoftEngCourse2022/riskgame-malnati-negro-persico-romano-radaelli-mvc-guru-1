import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Partita from "../pages/Partita";
import LayoutHome from "../component/layout/LayoutHome";
import Lobby from "../pages/Lobby";

function Root() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LayoutHome children={<Home />} />} />
        <Route path={"/partita/:id"} element={<Partita />} />
        <Route path="/lobby/:idPartita" element={<Lobby />} />
      </Routes>
    </Router>
  );
}

export default Root;
