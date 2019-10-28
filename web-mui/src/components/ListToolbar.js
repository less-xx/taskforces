import React, { useEffect, useState } from 'react';
import Paper from '@material-ui/core/Paper';
import Toolbar from '@material-ui/core/Toolbar';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import SearchIcon from '@material-ui/icons/Search';
import Select from '@material-ui/core/Select';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 150,
        borderRadius: 5,
        textAlign: 'center'
      },
    searchBar: {
        borderBottom: '1px solid rgba(0, 0, 0, 0.12)',
    },
    searchInput: {
        fontSize: theme.typography.fontSize,
    },
    block: {
        display: 'block',
    },
    newActionButton: {
        marginRight: theme.spacing(2),
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(3),
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        
    },
    contentShift: {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        
    },
}));

function ListToolbar ({newAction, sortByOptions, className}) {

    const classes = useStyles();
    const [sort, setSort] = React.useState({});
    
    const handleChangeSortBy = sortBy => event => {
        setSort({
          ...sort,
          sortBy: event.target.value,
        });
    };

    return (
        <Paper className={className}>
        <Toolbar>
            <Grid container spacing={2} alignItems="center">
                <Grid item>
                    <Button variant="outlined" color="primary" onClick={newAction} className={classes.newActionButton}>New</Button>
                </Grid>
                <Grid item>
                    <SearchIcon className={classes.block} color="inherit" />
                </Grid>
                <Grid item xs>
                    <TextField
                        fullWidth
                        placeholder="Search anything"
                        InputProps={{
                            disableUnderline: true,
                            className: classes.searchInput,
                        }}
                    />
                </Grid>
                {sortByOptions &&
                    <Grid item>
                        <FormControl className={classes.formControl}>
                        <InputLabel htmlFor="select-sort-by">Sort By</InputLabel>
                        <Select className={classes.sortBy}
                            value={sort.sortBy}
                            onChange={handleChangeSortBy('sortBy')}
                            inputProps={{
                                name: 'sortBy',
                                id: 'select-sort-by',
                            }}
                            >
                            {sortByOptions.map((s,idx)=><option key={idx} value={s.value}>{s.text}</option>)}
                        </Select>
                        </FormControl>
                    </Grid>
                }
                
            </Grid>
        </Toolbar>
        </Paper>
    )

}

export default ListToolbar