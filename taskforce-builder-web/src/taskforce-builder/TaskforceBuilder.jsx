import Blockly from 'node-blockly/browser';

function TaskforceBuilder() {

}

function getToolbox() {
    return <xml id="toolbox" style="display: none">
        <category name="Core">
            <category name="Control">
                <block type="controls_if"></block>
                <block type="controls_whileUntil"></block>
            </category>
            <category name="Logic">
                <block type="logic_compare"></block>
                <block type="logic_operation"></block>
                <block type="logic_boolean"></block>
            </category>
        </category>
        <category name="Custom">
            <block type="start"></block>
            <category name="Move">
                <block type="move_forward"></block>
                <block type="move_backward"></block>
            </category>
            <category name="Turn">
                <block type="turn_left"></block>
                <block type="turn_right"></block>
            </category>
        </category>
    </xml>;
}