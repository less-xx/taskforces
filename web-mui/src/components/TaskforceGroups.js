import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import {reloadGroups} from '../actions/TaskforceActions'

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

    return (
        
        <>
            <div>
                Taskforce groups
            </div>
        </>
    )
}

export default TaskforceGroups;
