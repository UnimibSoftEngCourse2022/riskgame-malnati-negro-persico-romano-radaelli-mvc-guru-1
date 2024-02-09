import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";



function Root() {
  return (
    <Router>
      <Routes>
          <Route path="/" element={<Home />} />
          <Route path={"/partita"} element={<Partita />} />
      </Routes>
    </Router>
  );
}

export default Root;
