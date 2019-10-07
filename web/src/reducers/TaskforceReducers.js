import { ActionTypes as TaskforceActions } from '../actions/TaskforceActions';

export const taskforceGroups = (state = [], action) => {
    switch (action.type) {
        case TaskforceActions.RELOAD_GROUPS:
            return action.groups;
        default:
            return state;
    }
}