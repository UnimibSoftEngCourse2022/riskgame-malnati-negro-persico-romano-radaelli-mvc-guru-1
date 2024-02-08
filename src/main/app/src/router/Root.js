import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Login from "../pages/Login";


function Root() {
  return (
    <Router>
      <Routes>
          <Route path="/" element={<Login />} />

      </Routes>
    </Router>
  );
}

export default Root;
