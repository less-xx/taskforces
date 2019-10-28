import { combineReducers } from 'redux';
import { toggleDrawer, activeNavigatorMenu, openDialog } from './LayoutReducers';
import { taskforceGroups, taskforces } from './TaskforceReducers';

export default combineReducers({
    toggleDrawer,
    activeNavigatorMenu,
    taskforceGroups,
    taskforces,
    openDialog,
});