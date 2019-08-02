import React, { Component } from 'react';
import './SideBar.css'

class MainConent extends Component {

    constructor(props) {
        super(props);
        this.myRef = React.createRef();
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="sb-main-content" ref={this.myRef}>
                {this.props.children}
            </div>
        );
    }

    getSize() {
        const node = this.myRef.current;
        return { width: node.offsetWidth, height: node.offsetHeight };
    }
}

export default MainConent;