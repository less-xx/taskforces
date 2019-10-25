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
import Link from '@material-ui/core/Link';

const useStyles = makeStyles(theme => ({
    button: {
        margin: theme.spacing(0),
    },
    execList: {
        width: '100%',
    },
    execItem: {
        borderWidth: 1,
        borderColor: '#CCCCCC',
        borderStyle: 'solid',
        borderRadius: 5,
        marginBottom: 5,
    },
    execStatus: {
        padding: theme.spacing(0),
    },
    inline: {
        display: 'inline',
    },
    execItemLine: {
        display: 'block',
    },
    section: {
        paddingBottom: theme.spacing(2),
    },
    viewLogLink: {
        float: 'right'
    }
}));

const queryTaskforceExecutions = (taskforceId, handleTaskExecutions, handleError) => {
    TaskforceService.queryTaskforceExecution({
        taskforceId: taskforceId,
        size: 2,
        sort: ["startTime,desc"]
    }, (response) => {
        console.log(response)
        handleTaskExecutions(response.content)
    }, (error) => {
        handleError(error)
    })
}
const TaskExecutionItem = ({ taskExecution }) => {
    const classes = useStyles();

    return (
        <>
            <ListItem alignItems="flex-start" className={classes.execItem}>
                <ListItemText primary={taskExecution.status} className={classes.execStatus}/>
                <ListItemText secondary={
                    <>
                        <span className={classes.execItemLine}>
                            <Typography
                                component="span"
                                variant="body2"
                                className={classes.inline}
                                color="textPrimary"
                            >
                                Start:
                            </Typography>
                            <Moment format="MM-DD HH:mm">{new Date(taskExecution.startTime)}</Moment>
                        </span>
                        <span className={classes.execItemLine}>
                            <Typography
                                component="span"
                                variant="body2"
                                className={classes.inline}
                                color="textPrimary"
                            >
                                End:
                            </Typography>
                            <Moment format="MM-DD HH:mm">{new Date(taskExecution.endTime)}</Moment>
                        </span>
                        <span className={classes.viewLogLink}>
                        <Link href="#" onClick={()=>{}} variant="body2" >
                            View Log
                        </Link>
                        </span>
                    </>
                } />
            </ListItem>
        </>
    )
}

function RunControlPanel({ taskforce }) {
    const classes = useStyles();
    const [taskExecutions, setTaskExecutions] = useState([])
    const [runningTaskExec, setRunningTaskExec] = useState({})
    console.log(runningTaskExec)

    const runTaskforce = () => {
        TaskforceService.runTaskforce(taskforce.id, (taskExec) => {
            setRunningTaskExec(taskExec)
        }, (error) => {
            console.log(error)
        })

    }

    const stopTaskforce = () => {
        if (runningTaskExec != null) {
            TaskforceService.stopTaskforce(taskforce.id, (response) => {
                console.log(response)
            }, (error) => {
                console.log(error)
            })
        }
    }

    useEffect(() => {
        queryTaskforceExecutions(taskforce.id, (execs) => {
            setTaskExecutions(execs)
            if (execs.length > 0) {
                if (execs[execs.length - 1].status === 'Running') {
                    console.log("Found running task execution.")
                    setRunningTaskExec(execs[execs.length - 1])
                } else {
                    console.log("No running taskforce")
                    setRunningTaskExec({})
                }
            }
        }, (error) => {
            console.log(error)
        })
    }, [])

    return (
        <div>
            <div className={classes.section}>
                <Typography variant="h5" component="h5">
                    Run
                </Typography>
                <ButtonGroup aria-label="small contained button group" fullWidth>
                    <Button startIcon={<PlayArrowOutlinedIcon />} color="default" onClick={runTaskforce} disabled={runningTaskExec.id != null}
                        className={classes.button}>
                        Run
                    </Button>
                    <Button startIcon={<StopOutlinedIcon />} color="default" onClick={stopTaskforce} disabled={runningTaskExec.id == null}
                        className={classes.button}>
                        Stop
                    </Button>
                </ButtonGroup>
            </div>
            <div className={classes.section}>
                <Typography variant="h5" component="h5">
                    Recent Executions
                </Typography>
                <List className={classes.execList} component="nav">
                    {taskExecutions.map(e => <TaskExecutionItem key={e.id} taskExecution={e} />)}
                </List>
            </div>
        </div>
    )
}

export default RunControlPanel