import React, { useEffect } from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';

import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
import ViewModuleIcon from '@material-ui/icons/ViewModule';
import DashboardIcon from '@material-ui/icons/Dashboard';
import DnsRoundedIcon from '@material-ui/icons/DnsRounded';
import PermMediaOutlinedIcon from '@material-ui/icons/PhotoSizeSelectActual';
import PublicIcon from '@material-ui/icons/Public';
import SettingsEthernetIcon from '@material-ui/icons/SettingsEthernet';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import TimerIcon from '@material-ui/icons/Timer';
import SettingsIcon from '@material-ui/icons/Settings';
import PhonelinkSetupIcon from '@material-ui/icons/PhonelinkSetup';
import { useDispatch, useSelector } from 'react-redux';
import { drawerWidth } from './themes/Default';
import { useHistory } from "react-router-dom";
import { activeNavigatorMenu } from './actions/LayoutActions';

const categories = [
    {
        id: 'Taskforces',
        children: [
            { id: 'Dashboard', icon: <DashboardIcon />, path: '/' },
            { id: 'Taskforce Groups', icon: <ViewModuleIcon />, path: '/taskforces' },
            { id: 'Database', icon: <DnsRoundedIcon /> },
            { id: 'Storage', icon: <PermMediaOutlinedIcon /> },
            { id: 'Hosting', icon: <PublicIcon /> },
            { id: 'Functions', icon: <SettingsEthernetIcon /> },
            { id: 'ML Kit', icon: <SettingsInputComponentIcon /> },
        ],
    },
    {
        id: 'Quality',
        children: [
            { id: 'Analytics', icon: <SettingsIcon /> },
            { id: 'Performance', icon: <TimerIcon /> },
            { id: 'Test Lab', icon: <PhonelinkSetupIcon /> },
        ],
    },
];

const useStyles = makeStyles(theme => ({
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
    },
    drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerClose: {
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: theme.spacing(6) + 1,
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(6) + 3,
        },
    },
    drawerHeader: {
        display: 'flex',
        alignItems: 'center',
        padding: theme.spacing(0, 1),
        ...theme.mixins.toolbar,
        justifyContent: 'flex-end',
    },
    menu: {
        paddingTop: theme.spacing(8),
    },
    categoryHeader: {
        paddingTop: theme.spacing(0),
        paddingBottom: theme.spacing(2),
    },
    categoryHeaderPrimary: {
        color: theme.palette.common.white,
    },
    item: {
        paddingTop: 1,
        paddingBottom: 1,
        color: 'rgba(255, 255, 255, 0.7)',
        '&:hover,&:focus': {
            backgroundColor: 'rgba(255, 255, 255, 0.08)',
        },
    },
    itemCategory: {
        backgroundColor: '#232f3e',
        boxShadow: '0 -1px 0 #404854 inset',
        paddingTop: theme.spacing(2),
        paddingBottom: theme.spacing(2),
    },
    firebase: {
        fontSize: 24,
        color: theme.palette.common.white,
    },
    itemActiveItem: {
        color: '#4fc3f7',
    },
    itemPrimary: {
        fontSize: 'inherit',
    },
    itemIcon: {
        minWidth: 'auto',
        marginRight: theme.spacing(2),
    },
    divider: {
        marginTop: theme.spacing(2),
    },
    hide: {
        display: 'none',
    },
}));

const currentCategoryId = (path) => {
    for (const cIdx in categories) {
        const children = categories[cIdx].children
        for (var eIdx in children) {
            if (children[eIdx].path === path) {
                return children[eIdx].id
            }
        }
    }
    return 'Dashboard'
}

function Navigator() {
    const classes = useStyles()
    const drawerOpen = useSelector(state => state.toggleDrawer)
    const activeNavigatorId = useSelector(state => state.activeNavigatorMenu)
    const dispatch = useDispatch()
    const history = useHistory()
    //console.log(history.location)

    useEffect(() => {
        const cid = currentCategoryId(history.location.pathname)
        console.log("Current category: "+cid)
        dispatch(activeNavigatorMenu(cid))
    })

    return (
        <Drawer variant="permanent" className={clsx(classes.drawer, {
            [classes.drawerOpen]: drawerOpen,
            [classes.drawerClose]: !drawerOpen,
        })}
            classes={{
                paper: clsx({
                    [classes.drawerOpen]: drawerOpen,
                    [classes.drawerClose]: !drawerOpen,
                }),
            }}
            open={drawerOpen}>

            <List className={classes.menu}>

                {categories.map(({ id, children }) => (
                    <React.Fragment key={id}>
                        <ListItem
                            className={clsx(classes.categoryHeader, !drawerOpen && classes.hide)}>
                            <ListItemText
                                classes={{
                                    primary: classes.categoryHeaderPrimary,
                                }}
                            >
                                {id}
                            </ListItemText>
                        </ListItem>
                        {children.map(({ id: childId, icon, path }) => (
                            <ListItem
                                key={childId}
                                button
                                className={clsx(classes.item, (childId === activeNavigatorId) && classes.itemActiveItem)}
                                onClick={e => {
                                    history.push(path);
                                    dispatch(activeNavigatorMenu(childId));
                                }}
                            >
                                <ListItemIcon className={classes.itemIcon}>{icon}</ListItemIcon>
                                <ListItemText
                                    classes={{
                                        primary: classes.itemPrimary,
                                    }}
                                >
                                    {childId}
                                </ListItemText>
                            </ListItem>
                        ))}

                        <Divider className={classes.divider} />
                    </React.Fragment>
                ))}
            </List>
        </Drawer>
    );
}

export default Navigator;