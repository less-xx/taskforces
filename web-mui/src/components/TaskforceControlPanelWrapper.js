import React from 'react';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';

import RunControlPanel from './panels/RunControlPanel';

function TaskforceControlPanelWrapper({taskforce}){

    return (
        <>
            <RunControlPanel taskforce={taskforce}/>
        </>
    )
    
}
export default TaskforceControlPanelWrapper
