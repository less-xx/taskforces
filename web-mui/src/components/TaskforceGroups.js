import React, { useEffect, useState } from 'react';
import { DrawerOpenWidth } from '../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import TaskforceGroupCard from './TaskforceGroupCard'
import EditTaskforceGroupDialog from './EditTaskforceGroupDialog'
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import Typography from '@material-ui/core/Typography';
import ListToolbar from './ListToolbar';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(3, 1),
        margin: theme.spacing(2, 2),
    },
    toolbar: {
        margin: theme.spacing(2, 4, 2, 4),
        backgroundColor: '#EFEFEF',
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
        left: DrawerOpenWidth + theme.spacing(4),
        transition: theme.transitions.create(['left'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }

}));

const sortByOptions = [{
    value: 'name',
    text: 'Name'
},{
    value: 'lastModifiedTime',
    text: 'Last Modified Time'
}]

function TaskforceGroups() {

    const dispatch = useDispatch();
    const taskforceGroups = useSelector(state => state.taskforceGroups);
    const [currentGroup, setCurrentGroup] = useState()
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
            <Breadcrumbs separator={<NavigateNextIcon fontSize="small" />} aria-label="breadcrumb">
                <Typography color="textSecondary">Taskforce</Typography>
                <Typography color="textPrimary">Groups</Typography>
            </Breadcrumbs>

            <ListToolbar sortByOptions={sortByOptions} className={classes.toolbar} newAction={newTaskgroup}/>

            <div className={classes.cards}>
                {groupCards}
            </div>

            <EditTaskforceGroupDialog refresh={reloadTaskforceGroups} group={currentGroup} />
        </>
    )
}

export default TaskforceGroups;
