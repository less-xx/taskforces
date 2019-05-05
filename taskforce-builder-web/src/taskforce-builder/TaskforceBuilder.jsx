import React, { Component } from 'react';
import Blockly from 'node-blockly/browser';
import './TaskforceBuilder.css'

class TaskforceBuilder extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            workspace: null
        };
    }

    componentDidMount() {
        var url = process.env.REACT_APP_URL_BLOCK_REGISTRIES;
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var toolboxXml = this.buildToolbox(result);
                    console.log(toolboxXml);
                    var mediaUrl = process.env.PUBLIC_URL + '/static/media/';
                    console.log(mediaUrl);
                    var workspace = Blockly.inject('blocklyDiv', {media: mediaUrl,  toolbox: toolboxXml });
                    Blockly.Xml.domToWorkspace(toolboxXml, workspace);
                    
                    this.setState({
                        isLoaded: true,
                        workspace: workspace
                    });

                    this.resizeWorkspace();
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );

        window.addEventListener('resize', this.resizeWorkspace.bind(this), false);
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

    render() {
        return (
           <div id="blocklyContainer">
                <div id="blocklyDiv" style={{ position: "absolute" }}></div>
           </div>
        );
    }

    resizeWorkspace(e) {
        
        if(this.state.workspace==null){
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
        console.log(x+", "+y+", "+blocklyArea.offsetWidth+", "+blocklyArea.offsetHeight);

    }
}

export default TaskforceBuilder;