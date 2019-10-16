import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Moment from 'react-moment';
import { useDispatch, useSelector } from 'react-redux';
import Link from '@material-ui/core/Link';
import { useHistory } from "react-router-dom";
import { activeNavigatorMenu } from '../actions/LayoutActions';

const useStyles = makeStyles(theme => ({
    card: {
        maxWidth: 320,
        minWidth: 200,
        minHeight: 200,
        maxHeight: 240,
        margin: theme.spacing(3, 1),
    },
    cardHeader: {
        margin: theme.spacing(0),
    },
    cardName: {
        minHeight: 60,
        maxHeight: 100,
        overflow: 'hidden',
    },
    cardDescription: {
        minHeight: 70,
        maxHeight: 80,
        overflow: 'hidden',
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    avatar: {
        backgroundColor: red[500],
    },
    titleLink: {
        margin: theme.spacing(2,0)
    }
}));



function TaskforceGroupCard(props) {

    const classes = useStyles();
    const dispatch = useDispatch();
    const history = useHistory()
    const taskforceGroup = useSelector(state => state.taskforceGroups[props.index])
    const lastUpdatedTime = (<Typography variant="subtitle2" color="textSecondary"><Moment format='LL'>{new Date(taskforceGroup.lastUpdatedTime)}</Moment></Typography>)
    const path = `/taskforce-groups/${taskforceGroup.id}`
    const title =  (
        <Link href="#" 
            onClick={e => {
                history.push(path);
            }}
            className={classes.titleLink}><Typography variant="subtitle1">{taskforceGroup.name}</Typography>
        </Link>
    )
    return (
        <Card className={classes.card}>
            <CardHeader
                action={
                    <IconButton aria-label="settings">
                        <MoreVertIcon />
                    </IconButton>
                }
                title={title}
                subheader={lastUpdatedTime}
                className={classes.cardHeader}
            />
            <CardContent className={classes.cardDescription}>
                <Typography variant="body2" color="textSecondary" component="p">
                    {taskforceGroup.description}
                </Typography>
            </CardContent>
            <CardActions >
                <IconButton aria-label="add to favorites">
                    <FavoriteIcon />
                </IconButton>
                <IconButton aria-label="edit" onClick={e => props.edit(taskforceGroup)}>
                    <EditIcon />
                </IconButton>
                <IconButton aria-label="edit">
                    <DeleteIcon />
                </IconButton>
            </CardActions>
        </Card>
    )
}

export default TaskforceGroupCard;