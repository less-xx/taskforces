import React, { Component } from 'react';
import { MdViewHeadline, MdClose } from 'react-icons/md';
import './SideBar.css'

class SideBar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: true,
            onExpandHandler: props.onExpand,
            id: props.id
        }
    }

    render() {
        var expandedClass = this.state.expanded ? "expanded" : "collapsed";
        var expandButton;
        if (this.state.expanded) {
            expandButton = <MdClose size="2em" onClick={this.collapse.bind(this)} />
        } else {
            expandButton = <MdViewHeadline size="2em" onClick={this.expand.bind(this)} />
        }
        return (
            <div className={"sidebar-container-right " + expandedClass} id={this.state.id}>
                <div className={"sidebar-navicon-right "}>
                    {expandButton}
                </div>
                <div>{this.props.children}</div>
            </div>
        );
    }

    expand() {
        var onExpandHandler = this.state.onExpandHandler;
        this.setState({ expanded: true });
        if (onExpandHandler) {
            onExpandHandler(true);
        }
    }

    collapse() {
        var onExpandHandler = this.state.onExpandHandler;
        this.setState({ expanded: false });
        if (onExpandHandler) {
            onExpandHandler(false);
        }
    }
}

export default SideBar;