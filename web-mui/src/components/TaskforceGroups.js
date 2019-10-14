import React, { useEffect } from 'react';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups } from '../actions/TaskforceActions'
import { TaskforceGroupCard } from './TaskforceGroupCard'

function TaskforceGroups() {

    const dispatch = useDispatch();
    const taskforceGroups = useSelector(state => state.taskforceGroups);
    console.log(taskforceGroups);

    useEffect(() => {

        TaskforceService.fetchTaskforceGroups((groups, pager) => {
            console.log(groups);
            dispatch(reloadGroups(groups, pager));
        }, (error) => {
            console.log(error);
        });

    }, []);

    const groupCards = taskforceGroups.map(tg => (
        <TaskforceGroupCard taskforceGroup={tg} />
    ))

    return (

        <>
            <Paper>
                <Typography variant="h5" component="h3">
                    Taskforce Group
                </Typography>
          
            </Paper>
        </>
    )
}

export default TaskforceGroups;
