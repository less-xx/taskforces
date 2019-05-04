import React, { Component } from 'react';
import Blockly from 'node-blockly/browser';
import {
    Container,
    Row,
    Col
} from 'react-bootstrap';

class TaskforceBuilder extends Component {

    componentDidMount() {
        var toolboxXml = `<xml id="toolbox" style="display: none">
                <category name="Control">
                    <block type="controls_if"></block>
                    <block type="controls_whileUntil"></block>
                </category>
                <category name="Logic">
                    <block type="logic_compare"></block>
                    <block type="logic_operation"></block>
                    <block type="logic_boolean"></block>
                </category>
        </xml>`;
        var xml = Blockly.Xml.textToDom(toolboxXml);
        console.log(xml);
        var workspace = Blockly.inject('blocklyDiv', { toolbox: xml });
        console.log(workspace);
        Blockly.Xml.domToWorkspace(xml, workspace);
    }
    render() {
        console.log("render()");
        return <div id="blocklyDiv" style={{ height: "600px", width: "800px" }}></div>;
    }
}

export default TaskforceBuilder;