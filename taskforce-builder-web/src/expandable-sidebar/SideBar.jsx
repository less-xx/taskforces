import React, { Component } from 'react';
import {
    Modal,
    Button,
    Form
} from 'react-bootstrap';
import { MdViewHeadline } from 'react-icons/md';
import './SideBar.css'

class SideBar extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="sidebar-container-right expanded">
                <div className="sidebar-navicon">
                    <MdViewHeadline size="2em"/>
                </div>
            </div>
        );
    }
}

export default SideBar;