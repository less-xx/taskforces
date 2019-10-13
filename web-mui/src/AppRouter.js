import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { createBrowserHistory } from "history";

import TaskforceGroups from './components/TaskforceGroups';
import Content from './Content';

function AppRouter () {

  const browserHistory = createBrowserHistory();

  return (
    <Router history={browserHistory} >
        <Route path="/" component={Content} />
        <Route path="/taskforces" component={TaskforceGroups} />
    </Router>
  )
}

export default AppRouter;