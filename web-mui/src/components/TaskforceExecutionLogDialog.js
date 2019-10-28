import React, { useState, useEffect } from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useDispatch, useSelector, shallowEqual } from 'react-redux';
import { TaskforceDialogTypes, openTaskforceDialog } from '../actions/TaskforceActions'
import TaskforceService from '../resources/TaskforceService';


const LINES_PER_PAGE = 100

function TaskforceExecutionLogDialog() {

    const dispatch = useDispatch();
    const [logs,setLogs] = useState('')
    const [start, setStart] = useState(0)
    const [lines, setLines] = useState(LINES_PER_PAGE)

    const dialogObj = useSelector(state => state.taskforceDialogs, shallowEqual);
    const showDialog = dialogObj.dialog === TaskforceDialogTypes.TASKFORCE_EXEC_LOGS ? dialogObj.open : false;

    const title = "Taskforce Execution Log"
    //console.log(dialogObj)
    const handleClose = ()=>{
        dispatch(openTaskforceDialog(TaskforceDialogTypes.TASKFORCE_EXEC_LOGS, false))
    }

    const fetchLogs = (execution) => {
        TaskforceService.getTaskforceExecutionLogsById(execution.id, start, lines, (logs)=>{
            setLogs(logs)
        }, (error)=>{
            console.log(error)
        })
    }

    useEffect(()=>{
        
        if(dialogObj.data!=null){
          
          fetchLogs(dialogObj.data)
        }
        
    },[showDialog])

    return (
        <Dialog
            open={showDialog}
            onClose={handleClose}
            scroll='paper'
            aria-labelledby="taskforce-execution-log"
            maxWidth='lg'
        >
        <DialogTitle id="taskforce-execution-log">{title}</DialogTitle>
        <DialogContent dividers={true}>
          <DialogContentText component="pre">
            {logs}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    )

}
export default TaskforceExecutionLogDialog;