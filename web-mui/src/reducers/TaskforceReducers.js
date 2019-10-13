import { TaskforceActionTypes } from '../actions/TaskforceActions';

export const taskforceGroups = (state = [], action) => {
    switch (action.type) {
        case TaskforceActionTypes.RELOAD_GROUPS:
            return action.groups;
        default:
            return state;
    }
}