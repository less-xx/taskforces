import React, { } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import { Formik } from 'formik';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useDispatch, useSelector, shallowEqual } from 'react-redux';
import { ResourceDialogTypes, openResourceDialog } from '../actions/ResourceActions'
import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import FilePathService from '../resources/FilePathService';

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
        helperText: "Input the name of the user defined file path"
    },
    Path: {
        name: "path",
        label: "Path",
        helperText: "Input the file system path"
    },
    Description: {
        name: "description",
        label: "Description",
        helperText: "Input the description of the user defined file path"
    }
}

const isNew = (filePath) => {
    return filePath.id == null || filePath.id === '';
}

function EditUserDefinedFilePathDialog({ refresh, filePath = {} }) {

    const classes = useStyles();
    const dispatch = useDispatch();
    const dialogObj = useSelector(state => state.openDialog, shallowEqual);
    const openEditTaskforceDialog = dialogObj.dialog === ResourceDialogTypes.EDIT_USER_DEFINED_FILE_PATH ? dialogObj.open : false;
    const title = isNew(filePath) ? 'New File Path' : 'Edit File Path'

    const handleClose = () => {
        dispatch(openResourceDialog(ResourceDialogTypes.EDIT_USER_DEFINED_FILE_PATH, false))
    };

    const saveFilePath = (values, setSubmitting) => {
        setSubmitting(true)
        if (isNew(values)) {
            FilePathService.createFileSystemPath(values, (response) => {
                console.log(response)
                refresh()
                handleClose()
            }, (error) => {
                console.log(error)
                setSubmitting(false)
            })

        } else {
            const { id, ...request } = values;
            FilePathService.updateFileSystemPath(id, request, (response) => {
                console.log(response)
                refresh()
                handleClose()
            }, (error) => {
                console.log(error)
                setSubmitting(false)
            })
        }
    }

    const validate = values => {
        let errors = {};
        if (!values.name || values.name === "") {
            errors.name = 'Name is required';
        }
        return errors;
    }

    const toFormValues = (filePath) => {
        return { id: filePath.id, name: filePath.name, path: filePath.path, description: filePath.description?filePath.description:'' }
    }

    return (
        <Dialog open={openEditTaskforceDialog} onClose={handleClose} aria-labelledby="form-dialog-title" maxWidth="sm" fullWidth={true}>
            <DialogTitle id="form-dialog-title">{title}</DialogTitle>
            <DialogContent>
                <Formik
                    enableReinitialize={true}
                    initialValues={toFormValues(filePath)}
                    validate={validate}
                    onSubmit={(values, { setSubmitting }) => {
                        saveFilePath(values, setSubmitting)
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
                                        name={Field.Path.name}
                                        label={Field.Path.label}
                                        value={values.path}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        required
                                        error={errors.path && errors.path !== ''}
                                        helperText={(errors.name && touched.name) ? errors.name : Field.Path.helperText}
                                    />
                                </FormControl>

                                <FormControl className={classes.formControl}>
                                    <TextField
                                        name={Field.Description.name}
                                        label={Field.Description.label}
                                        multiline
                                        rowsMax="4"
                                        value={values.description}
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

export default EditUserDefinedFilePathDialog