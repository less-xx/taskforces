import { TaskforceActionTypes } from '../actions/TaskforceActions';

export const taskforceGroups = (state = [], action) => {
    switch (action.type) {
        case TaskforceActionTypes.RELOAD_GROUPS:
            return action.groups;
        default:
            return state;
    }
}

export const taskforces = (state = {}, action) => {
    switch (action.type) {
        case TaskforceActionTypes.LOAD_GROUP_TASKFORCES:
            return action.taskforces;
        default:
            return state;
    }
}

