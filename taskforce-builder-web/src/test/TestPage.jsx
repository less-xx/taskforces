import React, { Component } from 'react';
import './TestPage.css'
import SideBar from '../expandable-sidebar/SideBar';

class TestPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showInProcess: false,
            containerSize: null,
            contentSize: null,
            sideBarWidth: null
        }
    }

    componentDidMount() {
        const node = this.refs.sideBarContainer;
        const containerSize = node.getDefaultSize();
        this.setState({ containerSize: containerSize });

        const sideBarCmp = this.refs.sideBar;
        const sideBarSize = sideBarCmp.getSize();
        this.setState({
            contentSize: {
                width: containerSize.width - sideBarSize.width - 10,
                height: containerSize.height
            },
            sideBarWidth: sideBarSize.width
        });
    }


    render() {
        return (
            <SideBar.Container ref="sideBarContainer" onResize={this.onSizeChange.bind(this)}>

                <SideBar.Content ref="sideBarContent" size={this.state.contentSize}>
                    Content
                </SideBar.Content>

                <SideBar onExpand={this.onExpand.bind(this)} ref="sideBar">
                    <div>
                        SideBar content
                    </div>
                </SideBar>


            </SideBar.Container>
        );
    }


    onExpand(expanded, sideBarWidth) {
        console.log(expanded + "," + sideBarWidth);
        const { containerSize } = this.state;
        this.setState({
            contentSize: {
                width: containerSize.width - sideBarWidth-10,
                height: containerSize.height
            },
            sideBarWidth: sideBarWidth
        });
    }

    onSizeChange(width, height) {
        console.log(width + ", " + height);
        var containerSize = { width: width, height: height };
        var contentSize = {
            width: containerSize.width - this.state.sideBarWidth,
            height: containerSize.height
        };

        this.setState({
            containerSize: containerSize,
            contentSize: contentSize
        });
    }
}

export default TestPage;