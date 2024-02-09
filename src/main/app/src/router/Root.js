import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Partita from "../pages/Partita";


function Root() {
  return (
    <Router>
      <Routes>
          <Route path="/" element={<Home />} />
          <Route path={"/partita/:id"} element={<Partita />} />
      </Routes>
    </Router>
  );
}

export default Root;
