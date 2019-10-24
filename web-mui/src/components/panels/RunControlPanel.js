import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import PlayArrowOutlinedIcon from '@material-ui/icons/PlayArrowOutlined';
import StopOutlinedIcon from '@material-ui/icons/StopOutlined';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles(theme => ({
    button: {
      margin: theme.spacing(0),
    },
  }));
  
function RunControlPanel () {
    const classes = useStyles();

    return (
        <>
        <Typography variant="h5" component="h5">
            Run
        </Typography>
        <ButtonGroup aria-label="small contained button group" fullWidth>
            <Button startIcon={<PlayArrowOutlinedIcon />} color="default"
                className={classes.button}>
                    Run
            </Button>
            <Button startIcon={<StopOutlinedIcon />} color="default"
                className={classes.button}>
                    Stop
            </Button>
        </ButtonGroup>
        </>
    )
}

export default RunControlPanel