import React, { useEffect, useState } from 'react';
import clsx from 'clsx';
import { drawerWidth } from '../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Tooltip from '@material-ui/core/Tooltip';
import { useParams } from "react-router-dom";

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
        top: theme.spacing(15),
        left: theme.spacing(9),
        transition: theme.transitions.create(['left'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    newButtonShift: {
        position: 'absolute',
        top: theme.spacing(15),
        left: drawerWidth+theme.spacing(4),
        transition: theme.transitions.create(['left'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }
}));

const newTaskforce = () => {
    
}

const editTaskforce = (taskforce) => {
    
}

function Taskforces(props) {
    const dispatch = useDispatch();
    const taskforces = useSelector(state => state.taskforces);
    const classes = useStyles();
    const drawerOpen = useSelector(state => state.toggleDrawer);
    //const history = useHistory()
    const { groupId } = useParams();
    const [taskforceGroupName, setTaskforceGroupName] = useState()
    
    const reloadGroupTaskforces = (groupId) => {
        TaskforceService.fetchGroupTaskforces(groupId, (taskforces, pager) => {
            console.log(taskforces);
        }, (error) => {
            console.log(error);
        });
    }

    const loadTaskforceGroup = (groupId) => {
        TaskforceService.fetchTaskforceGroupById(groupId, (group) => {
            console.log(group);
            setTaskforceGroupName(group.name);
        }, (error) => {
            console.log(error);
        });
    }

    if(groupId){
        loadTaskforceGroup(groupId)
    }

    useEffect(() => {
        if(groupId){
            reloadGroupTaskforces(groupId)
        }
    }, [taskforces]);

    return (
        <>
            <Typography variant="h5" component="h3">
                Taskforces - {taskforceGroupName}
            </Typography>

            <Tooltip title="New Taskforce" aria-label="new-taskforce">
                    <Fab size="large" aria-label="new-taskforce" 
                        className={clsx(classes.newButton, {[classes.newButtonShift]: drawerOpen})} 
                        onClick={newTaskforce}>
                        <AddIcon color="action" />
                    </Fab>
                </Tooltip>
        </>
    )
}

export default Taskforces