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
import Divider from '@material-ui/core/Divider';

const useStyles = makeStyles(theme => ({
    card: {
        margin: theme.spacing(3, 1),
    },
    cardHeader: {
        margin: theme.spacing(0),
    },
    cardName: {
        minHeight: 30,
        maxHeight: 50,
        overflow: 'hidden',
    },
    cardContent: {
        margin: theme.spacing(0),
        paddingTop: theme.spacing(0),
        minHeight: 20,
        maxHeight: 80,
        overflow: 'hidden',
    },
    cardContentItem: {
        margin: theme.spacing(0),
        padding: theme.spacing(1),
        backgroundColor: '#EEEEEE',
    },
    cardContentDescription: {
        margin: theme.spacing(0),
        padding: theme.spacing(1),
    },
    cardContentDivider: {
        marginBottom: theme.spacing(1),
        borderColor: '#FF0000'
    }
}));


function UserDefinedFilePathCard({ filePath, edit }) {
    const classes = useStyles();
    const lastUpdatedTime = (
        <Typography variant="body2" color="textSecondary" component="span">
            {filePath.updatedBy} - <Moment format='LL'>{new Date(filePath.lastUpdatedTime)}</Moment>
        </Typography>
    )

    const title = (
        <Link href="#"
            onClick={e => edit(filePath)}>
            <Typography variant="subtitle1">{filePath.name}</Typography>
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
            <CardContent className={classes.cardContent}>
                <Typography variant="body1" color="textPrimary" component="p" className={classes.cardContentItem}>
                    Path: {filePath.path}
                </Typography>
                <Divider light  />
                <Typography variant="body2" color="textSecondary" component="p" className={classes.cardContentDescription}>
                    Description: {filePath.description}
                </Typography>
            </CardContent>

            <CardActions >
                <IconButton aria-label="add to favorites">
                    <FavoriteBorderOutlinedIcon />
                </IconButton>
                <IconButton aria-label="edit" onClick={e => edit(filePath)}>
                    <EditOutlinedIcon />
                </IconButton>
                <IconButton aria-label="delete">
                    <DeleteOutlinedIcon />
                </IconButton>
            </CardActions>
        </Card>
    )
}

export default UserDefinedFilePathCard;