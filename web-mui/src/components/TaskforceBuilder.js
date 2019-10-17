import React, { useEffect, useState } from 'react';
import Blockly from 'node-blockly/browser';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import { useDispatch, useSelector } from 'react-redux';
import TaskforceService from '../resources/TaskforceService';
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import { useParams, useHistory } from "react-router-dom";
import Paper from '@material-ui/core/Paper';
import Drawer from '@material-ui/core/Drawer';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import IconButton from '@material-ui/core/IconButton';
import Button from '@material-ui/core/Button';
import Fab from '@material-ui/core/Fab';
import Tooltip from '@material-ui/core/Tooltip';
import clsx from 'clsx';

const controlPanelWidth = 240

const useStyles = makeStyles(theme => ({
    paper: {
        minHeight: '83vh',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(0),
        borderRadius: 5
    },
    drawer: {
        width: controlPanelWidth,
        flexShrink: 0,
    },
    drawerPaper: {
        width: controlPanelWidth,
        backgroundColor: 'white',
    },
    drawerContent: {
        marginTop: theme.spacing(8),
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
    },
    workspaceContainer:{
        backgroundColor: 'ligthgray',
        width: paper.width,
        height: theme.height.paper,
    },
    controlPanelButton: {
        position: 'absolute',
        top: theme.spacing(7) - 1,
        right: theme.spacing(2),
        transition: theme.transitions.create(['right'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    controlPanelButtonShift: {
        position: 'absolute',
        top: theme.spacing(7) - 1,
        right: theme.spacing(30) + 5,
        transition: theme.transitions.create(['right'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }
}))

const initBlocklyWorkspace = () => {

    TaskforceService.fetchTaskforceBlocks((toolbox) => {
        const mediaUrl = process.env.PUBLIC_URL + '/static/media/';
        var workspace = Blockly.inject('workspaceContainer', {
            media: mediaUrl,
            toolbox: toolbox,
            grid: {
                spacing: 20,
                length: 3,
                colour: '#ccc',
                snap: true
            },
            zoom: {
                controls: true,
                wheel: true,
                startScale: 1.0,
                maxScale: 2,
                minScale: 0.5,
                scaleSpeed: 1.2
            },
            trashcan: true
        }, (error) => {
            console.log(error)
        });
    })


}

const registerBlock = (blockType, blockDef) => {
    //console.log(blockDef);
    Blockly.Blocks[blockType] = {
        init: function () {
            this.jsonInit(blockDef);
            if (blockDef.hat) {
                this.hat = blockDef.hat;
            }
        }
    };
}

const initCustomBlockDefs = () => {
    TaskforceService.fetchCustomBlockDefinitions((result) => {
        result.forEach(r => {
            //console.log(r);
            registerBlock(r.type, r.definition);
            //var theme = Blockly.Theme(BlockStyles, CategoryStyles);
            //Blockly.setTheme(theme);
        });
    }, (error) => {
        console.log(error)
    })
}

const resizeWorkspace=(toWidth, toHeight)=>{
    console.log(toWidth)
}


function TaskforceBuilder(props) {
    const classes = useStyles();
    const history = useHistory()
    const taskforceGroup = props.location.state
    const [controlPanelOpen, setControlPanelOpen] = useState(true)
    //console.log(taskforceGroup)
    const workspaceRef = useRef(null);

    if (taskforceGroup == null) {
        history.push("/taskforce-groups")
    }

    useEffect(() => {
        initCustomBlockDefs()
        initBlocklyWorkspace()
    })

    return (
        <>
            <Breadcrumbs separator={<NavigateNextIcon fontSize="small" />} aria-label="breadcrumb">
                <Link color="inherit" href="#" onClick={e => history.push("/taskforce-groups")}>
                    Groups
                </Link>
                <Link color="inherit" href="#" onClick={e => history.push("/taskforce-groups/" + taskforceGroup.id)}>
                    {taskforceGroup.name}
                </Link>
                <Typography color="textPrimary">Untitled</Typography>
            </Breadcrumbs>

            <Paper className={classes.paper}>
                <div  ref={workspaceRef} className={classes.workspaceContainer}>
                </div>
            </Paper>

            <Drawer
                className={classes.drawer}
                variant="persistent"
                anchor="right"
                open={controlPanelOpen}
                classes={{
                    paper: classes.drawerPaper,
                }}
            >
                <div className={classes.drawerContent}>
                    Content
                </div>
            </Drawer>

            <Tooltip title="Expand/Collapse Control Panel" aria-label="expand-collapse-control-panel">
                <Fab size="small" aria-label="expand-collapse-control-panel"
                    className={clsx(classes.controlPanelButton, { [classes.controlPanelButtonShift]: controlPanelOpen })}
                    onClick={e => setControlPanelOpen(!controlPanelOpen)}
                >
                    {controlPanelOpen ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                </Fab>
            </Tooltip>
        </>
    )

}

export default TaskforceBuilder