import React, { Component } from 'react';
import ReactDOM from "react-dom";
import './TestPage.css'
import SideBar from '../expandable-sidebar/SideBar';

class TestPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showInProcess: false
        }
    }

    componentDidMount() {
        const _this = this;
        this.setState({ showInProcess: true }, () => {
            this.setState({
                showInProcess: false
            });
            var observer = new MutationObserver(function (mutations) {
                mutations.forEach(function (mutation) {
                    if (mutation.type === "attributes") {
                        var obj = mutation.target;
                        console.log(obj.style.width);
                    }

                });
            });
            //console.log(_this.refs.sidebar.domObject());
            var obj = document.getElementById("test_side_bar");
            observer.observe(obj, {
                attributes: true
            });
        });

    }


    render() {
        return (
            <SideBar.Container >

                <SideBar.Content ref="content">
                    Content
                </SideBar.Content>

                <SideBar onExpand={this.onExpand.bind(this)} ref="sidebar">
                    <div>
                        SideBar content
                    </div>
                </SideBar>


            </SideBar.Container>
        );
    }

    onExpand(expanded) {
        console.log(expanded);
    }
}

export default TestPage;