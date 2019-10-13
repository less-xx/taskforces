import { combineReducers } from 'redux';
import { toggleDrawer } from './LayoutReducers';
import {taskforceGroups} from './TaskforceReducers';

export default combineReducers({
    toggleDrawer,
    taskforceGroups
});