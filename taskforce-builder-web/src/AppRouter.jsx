import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Taskforces from './taskforces/Taskforces';
import TaskforceBuilder from './taskforce-builder/TaskforceBuilder';

function AppRouter() {
    return (
        <Router>
           <Route path="/" exact component={Taskforces} />
           <Route path="/taskforces" exact component={Taskforces} />
           <Route path="/taskforce-editor" exact component={TaskforceBuilder} />
        </Router>
    );
}

export default AppRouter;