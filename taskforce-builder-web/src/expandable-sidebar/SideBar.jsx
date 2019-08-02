import React, { Component } from 'react';
import { MdViewHeadline, MdClose } from 'react-icons/md';
import './SideBar.css';
import MainContent from './MainContent';
import MainContainer from './MainContainer';

class SideBar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: true,
            onExpandHandler: props.onExpand,
            size: null
        }
        this.sideBarRef = React.createRef();
    }

    componentDidMount() {
        console.log(this.domObject().offsetWidth);
    }

    componentDidUpdate() {
        console.log(this.domObject().offsetWidth);
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
            <div className={"sidebar-container-right " + expandedClass} ref={this.sideBarRef} id="test_side_bar">
                <div className={"sidebar-navicon-right "}>
                    {expandButton}
                </div>
                <div>{this.props.children}</div>
            </div>
        );
    }

    domObject() {
        return this.sideBarRef.current;
    }

    getSize() {
        const node = this.sideBarRef.current;
        return { width: node.offsetWidth, height: node.offsetHeight };
    }

    expand() {
        this.setState({ expanded: true }, () => {
            var onExpandHandler = this.state.onExpandHandler;
            if (onExpandHandler) {
                onExpandHandler(true);
            }
        });

    }

    collapse() {
        this.setState({ expanded: false }, () => {
            var onExpandHandler = this.state.onExpandHandler;
            if (onExpandHandler) {
                onExpandHandler(false);
            }
        });
    }
}


SideBar.Container = MainContainer;
SideBar.Content = MainContent;

export default SideBar;