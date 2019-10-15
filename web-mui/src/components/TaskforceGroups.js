import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import TaskforceGroupCard from './TaskforceGroupCard'
import EditTaskforceGroupDialog from './EditTaskforceGroupDialog'
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(2, 0),

    },
    newGroupButton: {
        margin: theme.spacing(3, 1),
    },

}));

function TaskforceGroups() {

    const dispatch = useDispatch();
    const taskforceGroups = useSelector(state => state.taskforceGroups);
    //console.log(taskforceGroups);
    const classes = useStyles();

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

    const groupCards = taskforceGroups.map((tg, i) => (
        <TaskforceGroupCard key={i} taskforceGroup={tg} />
    ))

    const newTaskgroup = () => {
        //console.log("new task group")
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE_GROUP, true))
    }

    return (
        <>
            <Typography variant="h5" component="h3">
                Taskforce Group
            </Typography>

            <div className={classes.cards}>
                <Fab size="large" aria-label="add" className={classes.newGroupButton} onClick={newTaskgroup}>
                    <AddIcon color="action" />
                </Fab>

                {groupCards}
            </div>

            <EditTaskforceGroupDialog refresh={reloadTaskforceGroups} />
        </>
    )
}

export default TaskforceGroups;
