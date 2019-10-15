import { combineReducers } from 'redux';
import { toggleDrawer, activeNavigatorMenu } from './LayoutReducers';
import { taskforceGroups, taskforceDialogs } from './TaskforceReducers';

export default combineReducers({
    toggleDrawer,
    activeNavigatorMenu,
    taskforceGroups,
    taskforceDialogs
});