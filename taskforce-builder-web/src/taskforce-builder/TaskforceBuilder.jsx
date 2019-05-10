import React, { Component } from 'react';
import Cookies from 'universal-cookie';
import Blockly from 'node-blockly/browser';
import './TaskforceBuilder.css';
import DataService from '../DataService';
import DataStore from '../DataStore';

class TaskforceBuilder extends Component {

    constructor(props) {
        super(props);
        const cookies = new Cookies();
        this.state = {
            error: null,
            isLoaded: false,
            workspace: null,
            taskforce: null,
            taskforceId: DataStore.currentTaskforceId ? DataStore.currentTaskforceId : cookies.get("currentTaskforceId")
        };
        this.toSaveList = [];
    }

    componentDidMount() {
        
        DataService.fetchTaskforceBlocks(
            (result) => {
                var toolboxXml = this.buildToolbox(result);
                //console.log(toolboxXml);
                var mediaUrl = process.env.PUBLIC_URL + '/static/media/';
                //console.log(mediaUrl);
                var workspace = Blockly.inject('blocklyDiv', { media: mediaUrl, toolbox: toolboxXml });
                //Blockly.Xml.domToWorkspace(toolboxXml, workspace);
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
                    this.setState({ isLoaded: true })
                },
                (error) => {
                    console.log(error);
                })
        }.bind(this), 10000);
    }


    buildToolbox(toolboxContent) {
        var xmlDoc = document.implementation.createDocument(null, "xml");
        var categories = Object.keys(toolboxContent);
        categories.forEach(c => {
            var blocks = toolboxContent[c];
            var categoryNode = xmlDoc.createElement("category");
            categoryNode.setAttribute("name", c);
            blocks.forEach(b => {
                var blockNode = xmlDoc.createElement("block");
                blockNode.setAttribute("type", b["type"]);
                categoryNode.appendChild(blockNode);

                if (b["def"] != null) {
                    this.registerBlock(b["type"], b["def"]);
                    console.log(b);
                }
            });
            xmlDoc.documentElement.appendChild(categoryNode);
        });
        return xmlDoc.documentElement;
    }

    registerBlock(blockType, blockDef) {
        Blockly.Blocks[blockType] = {
            init: function () {
                this.jsonInit(blockDef);
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
            <div id="blocklyContainer">
                <div id="blocklyDiv" style={{ position: "absolute" }}></div>
            </div>
        );
    }

    resizeWorkspace(e) {

        if (this.state.workspace == null) {
            return;
        }
        var blocklyArea = document.getElementById('blocklyContainer');
        var blocklyDiv = document.getElementById('blocklyDiv');
        // Compute the absolute coordinates and dimensions of blocklyArea.
        var element = blocklyArea;
        var x = 0;
        var y = 0;
        do {
            x += element.offsetLeft;
            y += element.offsetTop;
            element = element.offsetParent;
        } while (element);
        // Position blocklyDiv over blocklyArea.
        blocklyDiv.style.left = x + 'px';
        blocklyDiv.style.top = y + 'px';
        blocklyDiv.style.width = blocklyArea.offsetWidth + 'px';
        blocklyDiv.style.height = blocklyArea.offsetHeight + 'px';
        Blockly.svgResize(this.state.workspace);
        //console.log(x+", "+y+", "+blocklyArea.offsetWidth+", "+blocklyArea.offsetHeight);

    }

    onChangeWorkspace(e) {

        if (e instanceof Blockly.Events.Ui) {
            return;
        }

        var xml = Blockly.Xml.workspaceToDom(this.state.workspace);
        if (this.toSaveList.length === 0) {
            this.toSaveList.push(Blockly.Xml.domToText(xml));
        } else {
            this.toSaveList[0] = Blockly.Xml.domToText(xml);
        }
    }

    saveWorksace(xml) {
        var url = process.env.REACT_APP_URL_CREATE_TASKFORCE;
        var xmlStr = JSON.stringify({

        });
        fetch(url, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: xmlStr
        })
    }
}

export default TaskforceBuilder;