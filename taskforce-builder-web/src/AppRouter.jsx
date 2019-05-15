import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Taskforces from './taskforces/Taskforces';
import TaskforceBuilder from './taskforce-builder/TaskforceBuilder';
import CustomResourcePath from './custom-resource-path/CustomResourcePath';

function AppRouter() {
    return (
        <Router>
           <Route path="/" exact component={Taskforces} />
           <Route path="/taskforces" component={Taskforces} />
           <Route path="/taskforce-editor" component={TaskforceBuilder} />
           <Route path="/custom-resource" component={CustomResourcePath} />
        </Router>
    );
}

export default AppRouter;