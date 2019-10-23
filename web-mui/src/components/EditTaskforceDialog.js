import React, { } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import { Formik } from 'formik';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useDispatch, useSelector, shallowEqual } from 'react-redux';
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
        name: "name",
        label: "Name",
        helperText: "Input the name of the taskforce group"
    },
    Description: {
        name: "description",
        label: "Description",
        helperText: "Input the description of the taskforce group"
    }
}


function EditTaskforceDialog({ refresh, taskforce }) {

    const classes = useStyles();
    const dispatch = useDispatch();
    const dialogObj = useSelector(state => state.taskforceDialogs, shallowEqual);
    const openEditTaskforceDialog = dialogObj.dialog === TaskforceDialogTypes.EDIT_TASKFORCE ? dialogObj.open : false;
    const title = 'Edit Taskforce'

    const handleClose = () => {
        dispatch(openTaskforceDialog(TaskforceDialogTypes.EDIT_TASKFORCE, false))
    };

    const saveTaskforce = (values, setSubmitting) => {
        setSubmitting(true)
        const { id, ...request } = values;
        TaskforceService.updateTaskforce(id, request, (response) => {
            console.log(response)
            handleClose()
            refresh()
        }, (error) => {
            console.log(error)
            setSubmitting(false)
        })
    }

    const validate = values => {
        let errors = {};
        if (!values.name || values.name === "") {
            errors.name = 'Name is required';
        }
        return errors;
    }

    const toFormValues = (taskforce) => {
        return { id: taskforce.id, name: taskforce.name, description: taskforce.description }
    }

    return (
        <Dialog open={openEditTaskforceDialog} onClose={handleClose} aria-labelledby="form-dialog-title" maxWidth="sm" fullWidth={true}>
            <DialogTitle id="form-dialog-title">{title}</DialogTitle>
            <DialogContent>
                <Formik
                    enableReinitialize={true}
                    initialValues={toFormValues(taskforce)}
                    validate={validate}
                    onSubmit={(values, { setSubmitting }) => {
                        saveTaskforce(values, setSubmitting)
                    }}
                >
                    {({
                        values,
                        errors,
                        touched,
                        handleChange,
                        handleBlur,
                        handleSubmit,
                        isSubmitting,
                    }) => {
                        //console.log(values)
                        return (
                            <form className={classes.form} onSubmit={handleSubmit}>
                                <FormControl className={classes.formControl}>
                                    <TextField
                                        autoFocus
                                        name={Field.Name.name}
                                        label={Field.Name.label}
                                        value={values.name}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        required
                                        error={errors.name && errors.name !== ''}
                                        helperText={(errors.name && touched.name) ? errors.name : Field.Name.helperText}
                                    />
                                </FormControl>

                                <FormControl className={classes.formControl}>
                                    <TextField
                                        name={Field.Description.name}
                                        label={Field.Description.label}
                                        multiline
                                        rowsMax="4"
                                        value={values.description ? values.description : ''}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        helperText={Field.Description.helperText}
                                    />
                                </FormControl>

                                <DialogActions>
                                    <Button onClick={handleClose} color="primary">
                                        Cancel
                                    </Button>
                                    <Button type="submit" color="primary" disabled={isSubmitting || (errors.name && errors.name !== '')}>
                                        Save
                                    </Button>
                                </DialogActions>
                            </form>
                        )
                    }}
                </Formik>
            </DialogContent>

        </Dialog>
    )
}

export default EditTaskforceDialog