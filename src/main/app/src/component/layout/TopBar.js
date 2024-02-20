import React from "react";
import { Navbar } from "react-bootstrap";
import { useAuth } from "../../auth/AuthContext";
import { MdPerson4 } from "react-icons/md";
import { useParams } from "react-router-dom";

function TopBar() {
  const { user } = useAuth();
  const { id } = useParams();
  return (
    <header className="" style={{ background: "gray", height: "60px" }}>
      <Navbar bg="" variant="dark" className="p-2">
        <Navbar.Collapse className="justify-content-end">
          {user ? (
            <>
              <span className="text-white">{user}</span>
              <MdPerson4 style={{ color: "#ffffff", fontSize: "40px" }} />
            </>
          ) : (
            <>
              <span className="text-white">{id}</span>
              <MdPerson4 style={{ color: "#ffffff", fontSize: "40px" }} />
            </>
          )}
        </Navbar.Collapse>
      </Navbar>
    </header>
  );
}

export default TopBar;
