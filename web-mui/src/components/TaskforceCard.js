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

const useStyles = makeStyles(theme => ({
    card: {
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
}));


function TaskforceCard({ taskforce, edit }) {
    const classes = useStyles();
    const dispatch = useDispatch();
    const history = useHistory()
    const lastUpdatedTime = <Moment format='LL'>{new Date(taskforce.lastUpdatedTime)}</Moment>

    const title = (
        <Link href="#"
            onClick={e => edit(taskforce)}
            className={classes.titleLink}>
            <Typography variant="subtitle1">{taskforce.name}</Typography>
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
                    {taskforce.description}
                </Typography>
            </CardContent>

            <CardActions >
                <IconButton aria-label="add to favorites">
                    <FavoriteIcon />
                </IconButton>
                <IconButton aria-label="edit" onClick={edit}>
                    <EditIcon />
                </IconButton>
                <IconButton aria-label="delete">
                    <DeleteIcon />
                </IconButton>
            </CardActions>
        </Card>
    )
}

export default TaskforceCard;