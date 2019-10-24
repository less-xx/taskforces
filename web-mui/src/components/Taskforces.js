import React, { useEffect, useState } from 'react';
import clsx from 'clsx';
import { DrawerOpenWidth } from '../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { loadGroupTaskforces, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Tooltip from '@material-ui/core/Tooltip';
import { useParams, useHistory } from "react-router-dom";
import TaskforceCard from './TaskforceCard';
import EditTaskforceDialog from './EditTaskforceDialog';
import * as moment from 'moment';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(2, 0),
    },
    newButton: {
        position: 'absolute',
        top: theme.spacing(20),
        left: theme.spacing(9),
        transition: theme.transitions.create(['left', 'top'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    newButtonShift: {
        position: 'absolute',
        top: theme.spacing(20),
        left: DrawerOpenWidth + theme.spacing(4),
        transition: theme.transitions.create(['left', 'top'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    taskforceCards: {
        //backgroundColor: theme.palette.background.paper,
        margin: theme.spacing(2, 10),
    },
}));

function Taskforces(props) {
    const dispatch = useDispatch();
    const taskforces = useSelector(state => state.taskforces);
    const classes = useStyles();
    const drawerOpen = useSelector(state => state.toggleDrawer);
    const history = useHistory()
    const { groupId } = useParams();
    const [taskforceGroup, setTaskforceGroup] = useState(props.location.state != null ? props.location.state : {})
    const [currentTaskforce, setCurrentTaskforce] = useState({})

    const reloadGroupTaskforces = (groupId) => {
        TaskforceService.fetchGroupTaskforces(groupId, (taskforces, pager) => {
            console.log(taskforces);
            dispatch(loadGroupTaskforces({ values: taskforces, pager: pager }))
        }, (error) => {
            console.log(error);
        });
    }

    const loadTaskforceGroup = (groupId) => {
        TaskforceService.fetchTaskforceGroupById(groupId, (group) => {
            //console.log(group);
            setTaskforceGroup(group);
        }, (error) => {
            console.log(error);
        });
    }

    const editTaskforce = (taskforce) => {
        console.log(taskforce)
        history.push('/taskforce-builder', taskforce)
    }

    const editTaskforceProps = (taskforce) => {
        setCurrentTaskforce(taskforce)
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE, true))
    }

    const newTaskforce = () => {
        const suffix = moment().format("YYYY-MM-DD_HH-mm-ss")
        history.push('/taskforce-builder', { name: "Untitled_"+suffix, group: taskforceGroup })
    }

    const taskforcesComponent = () => {
        if (taskforces.values) {
            return (
                <div className={classes.taskforceCards}>
                    {
                        taskforces.values.map(t => {
                            return (<TaskforceCard key={t.id} taskforce={t} edit={editTaskforce} editProps={editTaskforceProps}/>)
                        })
                    }
                </div>
            )

        } else {
            return <></>
        }
    }

    const taskforceGroupName = () => {
        return taskforceGroup.name != null ? taskforceGroup.name : "Unknown"
    }

    useEffect(() => {
        if (groupId) {
            reloadGroupTaskforces(groupId)
        }
    }, [groupId]);

    useEffect(() => {
        if (taskforceGroup.name == null) {
            loadTaskforceGroup(groupId)
        }
    }, [groupId, taskforceGroup.name]);


    return (
        <>
            <Breadcrumbs separator={<NavigateNextIcon fontSize="small" />} aria-label="breadcrumb">
                <Link color="inherit" href="#" onClick={e => history.push("/taskforce-groups")}>
                    Groups
                </Link>
                <Typography color="textPrimary">{taskforceGroupName()}</Typography>
            </Breadcrumbs>
            <Typography variant="h3" component="h3">
                Taskforces
            </Typography>

            {taskforcesComponent()}

            <Tooltip title="New Taskforce" aria-label="new-taskforce">
                <Fab size="large" aria-label="new-taskforce"
                    className={clsx(classes.newButton, { [classes.newButtonShift]: drawerOpen })}
                    onClick={e => newTaskforce()}>
                    <AddIcon color="action" />
                </Fab>
            </Tooltip>

            <EditTaskforceDialog refresh={() => reloadGroupTaskforces(groupId)} taskforce={currentTaskforce} />
        </>
    )
}

export default Taskforces