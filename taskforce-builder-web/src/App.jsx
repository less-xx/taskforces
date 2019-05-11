import React from 'react';
import {
  Navbar,
  Nav,
  NavDropdown,
  Button,
  Form,
  FormControl
} from 'react-bootstrap';
import './App.css';
import logo from './logo.svg'
import AppRouter from './AppRouter'

function App() {
  return (

    <div className="App" id="appMain">
      <div className="App-header">
        <Navbar bg="dark" expand="lg" variant="dark">
          <Navbar.Brand href="#home">
            <img src={logo} className="App-logo" alt="logo" />
            {'React-Bootstrap'}
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <Nav.Link href="/taskforces">Taskforces</Nav.Link>
              <Nav.Link href="#link">Link</Nav.Link>
              <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
                <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Form inline>
              <FormControl type="text" placeholder="Search" className="mr-sm-2" />
              <Button variant="outline-success">Search</Button>
            </Form>
          </Navbar.Collapse>
        </Navbar>
      </div>
      <div className="App-main">
        <AppRouter />
      </div>
      <div className="App-footer">
      </div>
    </div>
  );
}

export default App;
