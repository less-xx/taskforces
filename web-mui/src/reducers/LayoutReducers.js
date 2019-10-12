import {LayoutActions } from '../actions/LayoutActions';

export const toggleDrawer = (state = true, action) => {
    switch (action.type) {
        case LayoutActions.TOGGLE_DRAWER:
            return action.drawerOpen;
        default:
            return state;
    }
}