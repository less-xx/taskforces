import React, { Component } from 'react';
import { MdViewHeadline, MdClose } from 'react-icons/md';
import './SideBar.css';
import MainContent from './MainContent';
import MainContainer from './MainContainer';

class SideBar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: props.expanded == null ? true : props.expanded,
            onExpandHandler: props.onExpand,
            expandedWidth: 200,
            collpasedWidth: 3
        };
        this.sideBarRef = React.createRef();
    }

    componentDidMount() {

    }

    componentDidUpdate() {

    }

    render() {
        //var expandedClass = this.state.expanded ? "expanded" : "collapsed";
        var expandButton;
        if (this.state.expanded) {
            expandButton = <MdClose size="2em" onClick={this.collapse.bind(this)} />
        } else {
            expandButton = <MdViewHeadline size="2em" onClick={this.expand.bind(this)} />
        }
        var width = this.state.expanded ? this.state.expandedWidth : this.state.collpasedWidth;
        return (
            <div className="sidebar-container-right " style={{ width: width + "px" }} ref={this.sideBarRef} >
                <div className={"sidebar-navicon-right "}>
                    {expandButton}
                </div>
                <div >{this.props.children}</div>
            </div>
        );
    }

    expand() {

        this.setState({ expanded: true }, () => {

            var onExpandHandler = this.state.onExpandHandler;
            if (onExpandHandler) {
                onExpandHandler(true, this.state.expandedWidth);
            }
        });

    }

    collapse() {
        this.setState({ expanded: false }, () => {
            var onExpandHandler = this.state.onExpandHandler;
            if (onExpandHandler) {
                onExpandHandler(false, this.state.collpasedWidth);
            }
        });
    }

    getSize() {
        const node = this.sideBarRef.current;
        return { width: node.offsetWidth, height: node.offsetHeight };
    }
}

SideBar.Container = MainContainer;
SideBar.Content = MainContent;

export default SideBar;