import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import FavoriteBorderOutlinedIcon from '@material-ui/icons/FavoriteBorderOutlined';
import EditOutlinedIcon from '@material-ui/icons/EditOutlined';
import DeleteOutlinedIcon from '@material-ui/icons/DeleteOutlined';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Moment from 'react-moment';
import Link from '@material-ui/core/Link';

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


function TaskforceCard({ taskforce, edit, editProps }) {
    const classes = useStyles();
    const lastUpdatedTime = (
        <Typography variant="subtitle2" color="textSecondary">
            {taskforce.updatedBy} - <Moment format='LL'>{new Date(taskforce.lastUpdatedTime)}</Moment>
        </Typography>
    )

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
                    <FavoriteBorderOutlinedIcon />
                </IconButton>
                <IconButton aria-label="edit" onClick={e => editProps(taskforce)}>
                    <EditOutlinedIcon />
                </IconButton>
                <IconButton aria-label="delete">
                    <DeleteOutlinedIcon />
                </IconButton>
            </CardActions>
        </Card>
    )
}

export default TaskforceCard;