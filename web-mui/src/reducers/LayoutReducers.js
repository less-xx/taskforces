import {LayoutActions } from '../actions/LayoutActions';

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