import React, { useEffect, useState } from 'react';
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import { DrawerOpenWidth } from '../../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import ListToolbar from '../ListToolbar';
import UserCredentialService from '../../resources/UserCredentialService';
import {openUserCredentialsDialog, UserCredentialsDialogTypes} from '../../actions/UserCredentialsActions';

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
    newButton: {
        position: 'absolute',
        top: theme.spacing(20),
        left: theme.spacing(9),
        transition: theme.transitions.create(['left', 'top'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    newButtonShift: {
        position: 'absolute',
        top: theme.spacing(14),
        left: DrawerOpenWidth + theme.spacing(5),
        transition: theme.transitions.create(['left', 'top'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    credCards: {
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

function UserCredentials() {
    const dispatch = useDispatch();
    const classes = useStyles();
    const drawerOpen = useSelector(state => state.toggleDrawer);
    const [credentials, setCredentials] = useState([])
    const [currentCredentialId, setCurrentCredentialId] = useState({})
    
    const editCredential = (credential) => {
        setCurrentCredentialId(credential.id)
        dispatch(openResourceDialog(UserCredentialsDialogTypes.EDIT_USER_CREDENTIALS, true))
    }

    const newCredentials = () => {
        dispatch(openUserCredentialsDialog(UserCredentialsDialogTypes.EDIT_USER_CREDENTIALS, true))
    }

    const credentialsComponent = () => {
        return (
            <div className={classes.filePathCards}>
                {
                    filePaths.map(t => {
                        return (<UserDefinedFilePathCard key={t.id} filePath={t} edit={editFilePath}/>)
                    })
                }
            </div>
        )
    }

    const reloadUserCredentials = () => {
        UserCredentialService.fetchCredentials((credentials, pager) => {
            console.log(credentials)
            setCredentials(credentials)
        }, (error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        reloadUserCredentials()
    }, []);

    return (
        <>
            <Breadcrumbs separator={<NavigateNextIcon fontSize="small" />} aria-label="breadcrumb">
                <Typography color="textPrimary">User Credentials</Typography>
            </Breadcrumbs>
           
           <ListToolbar sortByOptions={sortByOptions} className={classes.toolbar} newAction={newCredentials}/>

            {credentialsComponent()}


            <EditUserDefinedFilePathDialog  filePath={currentFilePath} refresh={reloadFilePaths}/>
        </>
    )
}

export default UserCredentials