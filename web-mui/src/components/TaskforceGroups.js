import React, { useEffect } from 'react';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups } from '../actions/TaskforceActions'
import TaskforceGroupCard from './TaskforceGroupCard'

const useStyles = makeStyles(theme => ({

    cards: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'flex-center',
      padding: theme.spacing(2, 0),
      
    },
  }));

function TaskforceGroups() {

    const dispatch = useDispatch();
    const taskforceGroups = useSelector(state => state.taskforceGroups);
    console.log(taskforceGroups);

    const classes = useStyles();

    useEffect(() => {

        TaskforceService.fetchTaskforceGroups((groups, pager) => {
            console.log(groups);
            dispatch(reloadGroups(groups, pager));
        }, (error) => {
            console.log(error);
        });

    }, []);

    const groupCards = taskforceGroups.map((tg,i) => (
        <TaskforceGroupCard key={i} taskforceGroup={tg} />
    ))

    return (
        <>
            <Typography variant="h5" component="h3">
                Taskforce Group
            </Typography>
        
            <div className={classes.cards}>  
            {groupCards}
        
            </div>
        </>
    )
}

export default TaskforceGroups;
