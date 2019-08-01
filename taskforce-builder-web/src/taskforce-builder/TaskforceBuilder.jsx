import React, { Component } from 'react';
import Cookies from 'universal-cookie';
import Blockly from 'node-blockly/browser';
import {
    Card,
} from 'react-bootstrap';
import DataService from '../DataService';
import DataStore from '../DataStore';
import Notifications, { notify } from 'react-notify-toast';
import { MdPlayArrow, MdStop, } from 'react-icons/md';
import styled from 'styled-components';
import SideBar from '../expandable-sidebar/SideBar';

import './TaskforceBuilder.css';

const Main = styled.main`
    position: relative;
    height: 100%;
    padding: 0px;
    margin-right: ${props => (props.expanded ? 240 : 8)}px;
    border: 1px dotted gray;
`;

const NavHeader = styled.div`
    display: ${props => (props.expanded ? 'block' : 'none')};
    white-space: nowrap;
    background-color: lightgray;
    height:48px;
`;

const NavTitle = styled.div`
    font-size: 1.2em;
    padding: 13px 0 0 6px;
`;

class TaskforceBuilder extends Component {

    constructor(props) {
        super(props);
        const cookies = new Cookies();
        this.state = {
            error: null,
            isLoaded: false,
            workspace: null,
            taskforce: null,
            taskforceId: DataStore.currentTaskforceId ? DataStore.currentTaskforceId : cookies.get("currentTaskforceId"),
            isRunning: false,
            showControlPanel: false,
            taskExecutionProgress: null
        };
        this.toSaveList = [];
        Blockly.HSV_SATURATION = 0.9;
        Blockly.HSV_VALUE = 0.8;
        this.taskExecMonitoringTimer = null;
    }

    componentDidUpdate() {
        this.resizeWorkspace();
    }

    componentDidMount() {

        DataService.fetchCustomBlockDefinitions((result) => {
            //console.log(result);
            result.forEach(r => {
                //console.log(r);
                this.registerBlock(r.type, r.definition);
                //var theme = Blockly.Theme(BlockStyles, CategoryStyles);
                //Blockly.setTheme(theme);
            });
        }, (error) => {
            console.log(error);
        });

        DataService.fetchTaskforceBlocks(
            (result) => {
                //console.log(result);
                var mediaUrl = process.env.PUBLIC_URL + '/static/media/';
                //console.log(mediaUrl); 

                var workspace = Blockly.inject('blocklyDiv', {
                    media: mediaUrl,
                    toolbox: result,
                    grid: {
                        spacing: 20,
                        length: 3,
                        colour: '#ccc',
                        snap: true
                    },
                    zoom: {
                        controls: true,
                        wheel: true,
                        startScale: 1.0,
                        maxScale: 2,
                        minScale: 0.5,
                        scaleSpeed: 1.2
                    },
                    trashcan: true
                });

                workspace.addChangeListener(this.onChangeWorkspace.bind(this));
                this.setState({
                    isLoaded: true,
                    workspace: workspace
                });

                const cookies = new Cookies();
                cookies.set('currentTaskforceId', this.state.taskforceId, { path: '/' });
                if (this.state.taskforceId) {
                    this.loadWorkspace();
                }
                this.resizeWorkspace();
            },
            (error) => {
                console.log(error);
            });

        window.addEventListener('resize', this.resizeWorkspace.bind(this), false);

        setInterval(function () {
            if (this.toSaveList.length === 0) {
                return;
            }
            if (this.state.taskforce == null) {
                console.log("taskforce is null");
                return;
            }
            var xml = this.toSaveList.pop();

            var request = {
                configuration: xml
            }
            console.log(request);
            DataService.updateTaskforce(this.state.taskforce.id, request,
                (taskforce) => {
                    this.setState({ isLoaded: true });
                    notify.show("Worksapce saved ...", "info");
                },
                (error) => {
                    console.log(error);
                    notify.show("Failed to save worksace ...", "error", 8000);
                })
        }.bind(this), 10000);

        DataService.queryTaskforceExecution(null, this.state.taskforceId, ["Running", "Waiting", "Stopping"], null, null, (response) => {
            console.log(response);
            if (response.totalElements === 0) {
                this.setState({ isRunning: false });
            } else {
                this.setState({ isRunning: true });
                this.expandCollapseControlPanel(true);
            }
        },
            (error) => {
                console.log(error);
            });

        var controlPanel = document.getElementById("controlPanel");
        controlPanel.addEventListener("webkitAnimationEnd", this.onControlPanelAnimationEnd);
        controlPanel.addEventListener("animationend", this.onControlPanelAnimationEnd);
    }

    startMonitor(taskforceExec) {
        if (taskforceExec.status === "Waiting" ||
            taskforceExec.status === "Running" ||
            taskforceExec.status === "Stopping") {

            this.taskExecMonitoringTimer = setInterval(function () {
                DataService.getTaskforceExecutionById(taskforceExec.id, (response) => {
                    this.setState({ taskExecutionProgress: response.progress });
                }, (error) => {
                    console.log(error);
                });
            }.bind(this), 2000);
        } else {
            if (this.taskExecMonitoringTimer) {
                clearInterval(this.taskExecMonitoringTimer);
            }
        }

    }

    registerBlock(blockType, blockDef) {
        //console.log(blockDef);
        Blockly.Blocks[blockType] = {
            init: function () {
                this.jsonInit(blockDef);
                if (blockDef.hat) {
                    this.hat = blockDef.hat;
                }
            }
        };
    }

    loadWorkspace() {
        DataService.fetchTaskforce(this.state.taskforceId,
            (taskforce) => {
                console.log(taskforce);

                var xml = Blockly.Xml.textToDom(taskforce.configuration);
                Blockly.Xml.domToWorkspace(xml, this.state.workspace);

                this.setState({
                    isLoaded: true,
                    taskforce: taskforce
                });
            },
            (error) => {
                console.log(error);
            });
    }

    render() {
        return (
            <div className="main-container" id="main-container">

                <div id="workspaceContainer">
                    <Notifications options={{
                        zIndex: 200, top: '60px', animationDuration: 200, timeout: 1000, colors: {
                            info: {
                                color: "#999999",
                                backgroundColor: '#FDFDFD'
                            }
                        }
                    }} />
                    <div id="blocklyDiv">
                    </div>
                    <div id="maskOverlay" style={{ display: this.state.isRunning ? 'block' : 'none' }}></div>
                </div>

                <SideBar id="controlPanel" onExpand={this.expandCollapseControlPanel.bind(this)}>
                    <div >
                        <MdPlayArrow size='2em' onClick={this.runWorkspace.bind(this)} className={+this.state.isRunning ? 'controlButton disabled' : 'controlButton active'} />
                        Run
                        <MdStop size='2em' onClick={this.stopWorkspace.bind(this)} className={this.state.isRunning ? 'controlButton active' : 'controlButton disabled'} />
                        Stop
                    </div>
                </SideBar>
            </div>

        );
    }

    resizeWorkspace(e) {

        if (this.state.workspace == null) {
            return;
        }
        var blocklyArea = document.getElementById('workspaceContainer');
        var container = document.getElementById('main-container');
        var controlPanel = document.getElementById('controlPanel');
        var blocklyDiv = document.getElementById('blocklyDiv');

        //console.log(controlPanel.offsetWidth);
        var x = blocklyArea.offsetLeft;
        var y = blocklyArea.offsetTop;

        blocklyDiv.style.left = x + 'px';
        blocklyDiv.style.top = y + 'px';

        var width = container.offsetWidth - controlPanel.offsetWidth;
        blocklyDiv.style.width = width + 'px';
        blocklyDiv.style.height = blocklyArea.offsetHeight + 'px';
        Blockly.svgResize(this.state.workspace);
        //console.log(x+", "+y+", "+blocklyArea.offsetWidth+", "+blocklyArea.offsetHeight);

    }

    onChangeWorkspace(e) {

        if (e instanceof Blockly.Events.Ui) {
            return;
        }

        var xmlStr = Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(this.state.workspace));
        if (this.state.taskforce) {
            if (this.state.taskforce.configuration === xmlStr) {
                return;
            }
        }
        if (this.toSaveList.length === 0) {
            this.toSaveList.push((xmlStr));
        } else {
            this.toSaveList[0] = xmlStr;
        }
    }

    runWorkspace() {
        //console.log("run workspace " + this.state.taskforceId);
        this.setState({ isRunning: true });
        DataService.runTaskforce(this.state.taskforceId,
            (taskforceExec) => {
                console.log(taskforceExec);
                if (taskforceExec.status === "Running" || taskforceExec.status === "Waiting") {
                    this.setState({ isRunning: true });
                }
            }, (error) => {
                console.log(error);
            });
    }
    stopWorkspace() {
        console.log("stop workspace " + this.state.taskforceId);
        DataService.stopTaskforce(this.state.taskforceId,
            (taskforceExec) => {
                console.log(taskforceExec);
                if (taskforceExec.status === "Running" || taskforceExec.status === "Waiting") {
                    this.setState({ isRunning: true });
                } else {
                    this.setState({ isRunning: false });
                }
            }, (error) => {
                console.log(error);
            });
    }

    onControlPanelAnimationEnd() {
        var controlPanel = document.getElementById('controlPanel');
        console.log(controlPanel.offsetWidth);
    }

    expandCollapseControlPanel(expanded) {
        //console.log(expanded);
        this.setState({ showControlPanel: expanded });
        //this.resizeWorkspace();

    }
}

export default TaskforceBuilder;