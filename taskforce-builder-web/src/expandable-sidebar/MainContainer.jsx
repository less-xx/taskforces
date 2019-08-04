import React, { Component } from 'react';
import ReactResizeDetector from 'react-resize-detector';
import './SideBar.css'

class MainContainer extends Component {

    constructor(props) {
        super(props);
        this.myRef = React.createRef();
        this.state = {
            resizeHandler: this.props.onResize
        }
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="sb-main-container" ref={this.myRef}>
                <ReactResizeDetector handleWidth handleHeight onResize={this.onResize.bind(this)} />
                {this.props.children}
            </div>
        );
    }

    onResize(width, height) {
        const { resizeHandler } = this.state;
        if (resizeHandler) {
            resizeHandler(width, height);
        }
    }
    getDefaultSize() {
        const node = this.myRef.current;
        return { width: node.offsetWidth, height: node.offsetHeight };
    }
}

export default MainContainer;