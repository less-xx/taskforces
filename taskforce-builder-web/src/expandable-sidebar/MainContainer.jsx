import React, { Component } from 'react';
import './SideBar.css'

class MainContainer extends Component {

    constructor(props) {
        super(props);
        this.myRef = React.createRef();
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="sb-main-container" ref={this.myRef}>
                {this.props.children}
            </div>
        );
    }
}

export default MainContainer;