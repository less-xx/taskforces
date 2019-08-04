import React, { Component } from 'react';
import './SideBar.css'

class MainConent extends Component {

    constructor(props) {
        super(props);
        this.myRef = React.createRef();
        this.state = {
            size: this.props.size == null ? { width: 10, height: 10 } : this.props.size
        }
    }

    componentDidMount() {
    }

    componentWillReceiveProps(props) {
        //console.log(props);
        this.setState({ size: props.size });
    }

    render() {
        const size = this.state.size;
        console.log(size);
        return (
            <div className="sb-main-content" ref={this.myRef} style={{ width: size.width, height: size.height }}>
                {this.props.children}
            </div>
        );
    }
}

export default MainConent;