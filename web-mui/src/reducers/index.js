import { combineReducers } from 'redux';
import { toggleDrawer, activeNavigatorMenu } from './LayoutReducers';
import { taskforceGroups, taskforces, taskforceDialogs } from './TaskforceReducers';

export default combineReducers({
    toggleDrawer,
    activeNavigatorMenu,
    taskforceGroups,
    taskforces,
    taskforceDialogs,
});