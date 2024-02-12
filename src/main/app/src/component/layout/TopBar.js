import React from "react";
import { Navbar } from "react-bootstrap";
import { useAuth } from "../../auth/AuthContext";

function TopBar() {
  const { user } = useAuth();
  return (
    <header className="" style={{ background: "gray", height: "60px" }}>
      <Navbar bg="" variant="dark" className="p-2">
        <Navbar.Collapse className="justify-content-end">
          {user ? (
            <span className="text-white">{user}</span>
          ) : (
            <span className="text-white">GenericUser</span>
          )}
        </Navbar.Collapse>
      </Navbar>
    </header>
  );
}

export default TopBar;
