import React, { Component } from 'react';
import {
    Modal,
    Button,
    Form
} from 'react-bootstrap';
import './TestPage.css'
import SideBar from '../expandable-sidebar/SideBar';

class TestPage extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="main-container">
                <SideBar />
            </div>
        );
    }
}

export default TestPage;