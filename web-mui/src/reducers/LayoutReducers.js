import {LayoutActions } from '../actions/LayoutActions';
import { TaskforceActionTypes } from '../actions/TaskforceActions';
import { ResourceActionTypes } from '../actions/ResourceActions';

export const toggleDrawer = (state = true, action) => {
    switch (action.type) {
        case LayoutActions.TOGGLE_DRAWER:
            return action.drawerOpen;
        default:
            return state;
    }
}

export const activeNavigatorMenu = (state = 'Dashboard', action) => {
    switch (action.type) {
        case LayoutActions.ACTIVE_NAVIATOR_MENU:
            return action.id;
        default:
            return state;
    }
}

export const openDialog = (state = {}, action) => {

    const { type, ...args } = action
    switch (type) {
        case TaskforceActionTypes.OPEN_TASKFORCE_DIALOG:
            return args;
        case ResourceActionTypes.OPEN_RESOURCE_DIALOG:
            return args;
        default:
            return state;
    }
}