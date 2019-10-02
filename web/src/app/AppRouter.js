import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import TaskforceGroupManager from '../containers/TaskforceGroupManager';

export const AppRouter = () => (
    <Router>
        <Route path="/" component={TaskforceGroupManager} />
        <Route path="/taskforces" component={TaskforceGroupManager} />
    </Router>
)