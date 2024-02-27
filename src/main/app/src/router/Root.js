import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import LayoutHome from "../component/layout/LayoutHome";
import Lobby from "../pages/Lobby";
import Mappa from "../pages/Mappa";
import LayoutMappa from "../component/layout/LayoutMappa";
import Partita from "../pages/Partita";

function Root() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LayoutHome children={<Home />} />} />
        <Route path={"/partita/:id"} element={<Partita />} />
        <Route path="/lobby/:idPartita" element={<Lobby />} />
        <Route
          path="/mappa/:id"
          element={<LayoutMappa children={<Mappa />} />}
        />
      </Routes>
    </Router>
  );
}

export default Root;
