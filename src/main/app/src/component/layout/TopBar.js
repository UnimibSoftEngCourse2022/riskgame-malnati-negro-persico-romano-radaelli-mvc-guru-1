
		import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';

function TopBar() {
  return (
    <Navbar bg="dark" variant="dark">
     <Navbar.Collapse className="justify-content-end">
        <Navbar.Text style = {{marginRight: '20px'}}>
          Utente
        </Navbar.Text>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default TopBar;
