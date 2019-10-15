import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useDispatch, useSelector } from 'react-redux';
import { TaskforceDialogTypes, openTaskforceDialog } from '../actions/TaskforceActions'
import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import TaskforceService from '../resources/TaskforceService';

const useStyles = makeStyles(theme => ({
    form: {
        display: 'flex',
        flexDirection: 'column',
        margin: 'auto',
        fullWidth: true
    },
    formControl: {
        margin: theme.spacing(1),
        fullWidth: true
    },
}));

function EditTaskforceGroupDialog({ refresh }) {

    const classes = useStyles();
    const dispatch = useDispatch();
    const dialogObj = useSelector(state => state.taskforceDialogs);
    const openEditTaskforceDialog = dialogObj.dialog === TaskforceDialogTypes.EDIT_TASKFORCE_GROUP ? dialogObj.open : false;
    const taskforceGroup = dialogObj.data ? dialogObj.data : {}
    const title = dialogObj.data ? 'Edit Taskforce Group' : 'New Taskforce Group'
    const [values, setValues] = React.useState(taskforceGroup);

    const handleClose = () => {
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE_GROUP, false))
    };

    const handleChange = name => event => {
        setValues({ ...values, [name]: event.target.value });
    };

    const saveTaskforceGroup = () => {
        if (dialogObj.data) {

        } else {
            TaskforceService.createTaskforceGroup(values, (response) => {
                console.log(response)
                handleClose()
                refresh()
            }, (error) => {
                console.log(error)
            })
        }
    }

    return (
        <Dialog open={openEditTaskforceDialog} onClose={handleClose} aria-labelledby="form-dialog-title" maxWidth="sm" fullWidth={true}>
            <DialogTitle id="form-dialog-title">{title}</DialogTitle>
            <DialogContent>
                <form className={classes.form}>
                    <FormControl className={classes.formControl}>
                        <TextField
                            autoFocus
                            id="taskforce-group-name"
                            label="Name"
                            value={taskforceGroup.name}
                            onChange={handleChange('name')}
                            required
                            helperText="Input the name of the taskforce group"
                        />
                    </FormControl>

                    <FormControl className={classes.formControl}>
                        <TextField
                            id="taskforce-group-desc"
                            label="Description"
                            multiline
                            rowsMax="4"
                            value={taskforceGroup.description}
                            onChange={handleChange('description')}
                            helperText="Input the description of the taskforce group"
                        />
                    </FormControl>
                </form>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">
                    Cancel
                </Button>
                <Button onClick={saveTaskforceGroup} color="primary">
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default EditTaskforceGroupDialog