import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import { loadGroupTaskforces, openTaskforceDialog, TaskforceDialogTypes } from '../actions/TaskforceActions'
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import { useParams, useHistory } from "react-router-dom";
import TaskforceCard from './TaskforceCard';
import EditTaskforceDialog from './EditTaskforceDialog';
import * as moment from 'moment';
import ListToolbar from './ListToolbar';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(2, 0),
    },
    toolbar: {
        margin: theme.spacing(2, 3, 2, 3),
        backgroundColor: '#EFEFEF',
    },
    taskforceCards: {
        //backgroundColor: theme.palette.background.paper,
        margin: theme.spacing(2, 2),
    },
}));

const sortByOptions = [{
    value: 'name',
    text: 'Name'
},{
    value: 'lastModifiedTime',
    text: 'Last Modified Time'
}]


function Taskforces(props) {
    const dispatch = useDispatch();
    const taskforces = useSelector(state => state.taskforces);
    const classes = useStyles();
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
                <Typography color="textSecondary">Taskforce</Typography>
                <Link color="inherit" href="#" onClick={e => history.push("/taskforce-groups")}>
                    Groups
                </Link>
                <Typography color="textPrimary">{taskforceGroupName()}</Typography>
            </Breadcrumbs>

            <ListToolbar sortByOptions={sortByOptions} className={classes.toolbar} newAction={newTaskforce}/>

            {taskforcesComponent()}

            <EditTaskforceDialog refresh={() => reloadGroupTaskforces(groupId)} taskforce={currentTaskforce} />
        </>
    )
}

export default Taskforces