import React, { useState, useEffect } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useDispatch, useSelector } from 'react-redux';
import { TaskforceDialogTypes, openTaskforceDialog } from '../actions/TaskforceActions'
import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
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

const Field = {
    Name: {
        id: "name",
        label: "Name",
        helperText: "Input the name of the taskforce group",
        errorTextRequired: "Name is required"
    },
    Description: {
        id: "description",
        label: "Description",
        helperText: "Input the description of the taskforce group"
    }
}

function EditTaskforceGroupDialog({ refresh }) {

    const classes = useStyles();
    const dispatch = useDispatch();
    const dialogObj = useSelector(state => state.taskforceDialogs);
    const openEditTaskforceDialog = dialogObj.dialog === TaskforceDialogTypes.EDIT_TASKFORCE_GROUP ? dialogObj.open : false;
    const taskforceGroup = dialogObj.data ? dialogObj.data : {}
    const title = dialogObj.data ? 'Edit Taskforce Group' : 'New Taskforce Group'

    const [values, setValues] = useState({})

    const [fieldError, setFieldError] = useState({});
    const [isInitForm, setInitForm] = useState(true);


   

    const handleClose = () => {
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE_GROUP, false))
    };

    const handleChange = name => event => {
        const newValues = { ...taskforceGroup, [name]: event.target.value }
        console.log(newValues)
        setValues(newValues)
        if(Field.Name.id === name) {
            if(newValues[Field.Name.id]===''){
                setFieldError({
                    id: Field.Name.id, 
                    errorMessage: Field.Name.errorTextRequired
                })
            }else{
                setFieldError({})
            }
        }
        setInitForm(false);
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
                            autoFocus={true}
                            id="taskforce-group-name"
                            label= {Field.Name.label}
                            value={taskforceGroup.name}
                            onChange={handleChange(Field.Name.id)}
                            required={true}
                            error = {fieldError.id === Field.Name.id}
                            helperText={ fieldError.id === Field.Name.id ? fieldError.errorMessage : Field.Name.helperText }
                        />
                    </FormControl>

                    <FormControl className={classes.formControl}>
                        <TextField
                            id="taskforce-group-desc"
                            label={Field.Description.label}
                            multiline
                            rowsMax="4"
                            value={taskforceGroup.description}
                            onChange={handleChange(Field.Description.id)}
                            helperText={ fieldError.id === Field.Description.id ? fieldError.errorMessage : Field.Description.helperText }
                        />
                    </FormControl>
                </form>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">
                    Cancel
                </Button>
                <Button onClick={saveTaskforceGroup} color="primary" disabled={ isInitForm || fieldError.id!=null}>
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default EditTaskforceGroupDialog