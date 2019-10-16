import React from 'react';
import { ThemeProvider } from '@material-ui/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Navigator from './Navigator';
import Header from './Header';
import { theme } from './themes/Default'
import Copyright from './Copyright'
import { makeStyles } from '@material-ui/core/styles';
import { Router, Route, Switch } from "react-router-dom";
import { createBrowserHistory } from "history";
import { routes } from './routes';

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

  const content = (
    <Switch>
      {routes.map((route, i) =>
        <Route key={i} exact={route.exact ? true : false} path={route.path} component={route.content}>
        </Route>
      )}
    </Switch>
  )

  const browserHistory = createBrowserHistory();

  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        <Router history={browserHistory}>
          <Navigator />

          <div className={classes.app}>
            <Header />
            <CssBaseline />
            <main className={classes.main}>
              {content}
            </main>

            <footer className={classes.footer}>
              <Copyright />
            </footer>

          </div>

        </Router>
      </div>
    </ThemeProvider>
  );
}

export default App;