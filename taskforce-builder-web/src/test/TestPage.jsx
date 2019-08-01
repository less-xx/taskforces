import React, { Component } from 'react';
import './TestPage.css'
import SideBar from '../expandable-sidebar/SideBar';

class TestPage extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="main-container">
                <div className="main-content">content</div>
                <SideBar onExpand={this.onExpand}>
                    <div>
                        SideBar content
                    </div>
                </SideBar>


            </div>
        );
    }

    onExpand(expanded) {
        console.log(expanded);
    }
}

export default TestPage;