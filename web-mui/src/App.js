import React from 'react';
import { ThemeProvider } from '@material-ui/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Navigator from './Navigator';
import Header from './Header';
import { theme } from './themes/Default'
import Copyright from './Copyright'
import { makeStyles } from '@material-ui/core/styles';
import AppRouter from './AppRouter';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    minHeight: '100vh',
  },
  app: {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
  },
  main: {
    flex: 1,
    padding: theme.spacing(10, 4),
    background: '#eaeff1',
  },
  footer: {
    padding: theme.spacing(2),
    background: '#eaeff1',
  },

}));

function App() {
  const classes = useStyles();

  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        
        <Navigator />

        <div className={classes.app}>
          <Header />
          <CssBaseline />
          <main className={classes.main}>
            <AppRouter />
          </main>

          <footer className={classes.footer}>
            <Copyright />
          </footer>

        </div>
      </div>
    </ThemeProvider>
  );
}

export default App;