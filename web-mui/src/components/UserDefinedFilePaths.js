import React, { useEffect, useState } from 'react';
import clsx from 'clsx';
import { DrawerOpenWidth } from '../themes/Default';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import FilePathService from '../resources/FilePathService';
import { openResourceDialog, ResourceDialogTypes } from '../actions/ResourceActions'
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Tooltip from '@material-ui/core/Tooltip';
import UserDefinedFilePathCard from './UserDefinedFilePathCard';
import EditUserDefinedFilePathDialog from './EditUserDefinedFilePathDialog';

const useStyles = makeStyles(theme => ({

    cards: {
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'flex-center',
        padding: theme.spacing(2, 0),
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
        top: theme.spacing(20),
        left: DrawerOpenWidth + theme.spacing(4),
        transition: theme.transitions.create(['left', 'top'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    filePathCards: {
        //backgroundColor: theme.palette.background.paper,
        margin: theme.spacing(2, 10),
    },
}));

function UserDefinedFilePaths() {
    const dispatch = useDispatch();
    const classes = useStyles();
    const drawerOpen = useSelector(state => state.toggleDrawer);
    const [filePaths, setFilePaths] = useState([])
    const [currentFilePath, setCurrentFilePath] = useState({})
    
    const editFilePath = (filePath) => {
        console.log(filePath)
    }

    const newFilePath = () => {
        dispatch(openResourceDialog(ResourceDialogTypes.EDIT_USER_DEFINED_FILE_PATH, true))
    }

    const filePathsComponent = () => {
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

    const reloadFilePaths = () => {
        FilePathService.fetchFileSystemPaths((filePaths, pager) => {
            console.log(filePaths)
            setFilePaths(filePaths)
        }, (error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        reloadFilePaths()
    }, []);

    return (
        <>
            <Breadcrumbs separator={<NavigateNextIcon fontSize="small" />} aria-label="breadcrumb">
                <Typography color="textPrimary">Resources</Typography>
                <Typography color="textPrimary">User Defined File Paths</Typography>
            </Breadcrumbs>
           

            {filePathsComponent()}

            <Tooltip title="New File Path" aria-label="new-file-path">
                <Fab size="large" aria-label="new-file-path"
                    className={clsx(classes.newButton, { [classes.newButtonShift]: drawerOpen })}
                    onClick={e => newFilePath()}>
                    <AddIcon color="action" />
                </Fab>
            </Tooltip>

            <EditUserDefinedFilePathDialog  filePath={currentFilePath} refresh={reloadFilePaths}/>
        </>
    )
}

export default UserDefinedFilePaths