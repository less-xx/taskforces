import React, { useEffect, useState } from 'react';
import clsx from 'clsx';
import { DrawerOpenWidth } from '../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import TaskforceGroupCard from './TaskforceGroupCard'
import EditTaskforceGroupDialog from './EditTaskforceGroupDialog'
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Tooltip from '@material-ui/core/Tooltip';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(3, 1),

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
        left: DrawerOpenWidth+theme.spacing(4),
        transition: theme.transitions.create(['left'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }

}));

function TaskforceGroups() {

    const dispatch = useDispatch();
    const taskforceGroups = useSelector(state => state.taskforceGroups);
    const [currentGroup, setCurrentGroup] = useState()
    const classes = useStyles();
    const drawerOpen = useSelector(state => state.toggleDrawer);

    const reloadTaskforceGroups = () => {
        TaskforceService.fetchTaskforceGroups((groups, pager) => {
            console.log(groups);
            dispatch(reloadGroups(groups, pager));
        }, (error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        reloadTaskforceGroups()
    }, []);

    const newTaskgroup = () => {
        setCurrentGroup({ id: '', name: '', description: '' })
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE_GROUP, true))
    }

    const editTaskgroup = (group) => {
        setCurrentGroup(group)
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE_GROUP, true))
    }

    const groupCards = taskforceGroups.map((tg, i) => (
        <TaskforceGroupCard key={i} index={i} edit={editTaskgroup} />
    ))

    return (
        <>
            <Typography variant="h5" component="h3">
                Taskforce Groups
            </Typography>

            <div className={classes.cards}>
                <Tooltip title="New Taskforce Group" aria-label="new-taskforce-group">
                    <Fab size="large" aria-label="new-taskforce-group" 
                        className={clsx(classes.newButton, {[classes.newButtonShift]: drawerOpen})} 
                        onClick={newTaskgroup}
                    >
                        <AddIcon color="action" />
                    </Fab>
                </Tooltip>
                {groupCards}
            </div>

            <EditTaskforceGroupDialog refresh={reloadTaskforceGroups} group={currentGroup}/>
        </>
    )
}

export default TaskforceGroups;
