import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import PlayArrowOutlinedIcon from '@material-ui/icons/PlayArrowOutlined';
import StopOutlinedIcon from '@material-ui/icons/StopOutlined';
import Typography from '@material-ui/core/Typography';
import TaskforceService from '../../resources/TaskforceService';
import Moment from 'react-moment';

const useStyles = makeStyles(theme => ({
    button: {
      margin: theme.spacing(0),
    },
    execList: {
        width: '100%',
    },
    inline: {
        display: 'inline',
    },
  }));

const queryTaskforceExecutions = (taskforceId, handleTaskExecutions, handleError)=>{
    TaskforceService.queryTaskforceExecution({
        taskforceId: taskforceId,
        size: 1,
        sort: ["startTime,desc"]
    }, (response) => {
        console.log(response)
        handleTaskExecutions(response.content)
    },(error) => {
        handleError(error)
    })
}
const TaskExecutionItem = ({taskExecution})=>{
    const classes = useStyles();

    return (
        <>
            <ListItem alignItems="flex-start">
                <ListItemText primary={taskExecution.status}/>
                <ListItemText secondary={
                    <React.Fragment>
                    <Typography
                      component="span"
                      variant="body2"
                      className={classes.inline}
                      color="textPrimary"
                    >
                      Start:
                    </Typography>
                    <Moment format="MM-DD HH:mm">{new Date(taskExecution.startTime)}</Moment>
                  </React.Fragment>
                }/>
            </ListItem>
            <Divider variant="inset" component="li" />
        </>
    )
}
  
function RunControlPanel ({taskforce}) {
    const classes = useStyles();
    const [taskExecutions, setTaskExecutions] = useState([])
    const [runningTaskExec, setRunningTaskExec] = useState({})
    console.log(runningTaskExec)

    const runTaskforce = ()=>{
        TaskforceService.runTaskforce(taskforce.id, (taskExec)=>{
            setRunningTaskExec(taskExec)
        },(error)=>{
            console.log(error)
        })

    }

    const stopTaskforce = ()=>{
        if(runningTaskExec!=null){
            TaskforceService.stopTaskforce(taskforce.id,(response)=>{
                console.log(response)
            },(error)=>{
                console.log(error)
            })
        }
    }

    useEffect(()=>{
        queryTaskforceExecutions(taskforce.id, (execs)=>{
            setTaskExecutions(execs)
            if(execs.length>0){
                if(execs[execs.length-1].status==='Running'){
                    console.log("Found running task execution.")
                    setRunningTaskExec(execs[execs.length-1])
                }else{
                    console.log("No running taskforce")
                    setRunningTaskExec({})
                }
            }
        },(error)=>{
            console.log(error)
        })
    },[])

    return (
        <>
        <Typography variant="h5" component="h5">
            Run
        </Typography>
        <ButtonGroup aria-label="small contained button group" fullWidth>
            <Button startIcon={<PlayArrowOutlinedIcon />} color="default" onClick={runTaskforce} disabled={runningTaskExec.id!=null}
                className={classes.button}>
                    Run
            </Button>
            <Button startIcon={<StopOutlinedIcon />} color="default" onClick={stopTaskforce} disabled={runningTaskExec.id==null}
                className={classes.button}>
                    Stop
            </Button>
        </ButtonGroup>

        <List className={classes.execList}>
            {taskExecutions.map(e=><TaskExecutionItem  key={e.id} taskExecution={e}/>)}
        </List>
        </>
    )
}

export default RunControlPanel