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

export const taskforceDialogs = (state = {}, action) => {

    const { type, ...args } = action
    switch (type) {
        case TaskforceActionTypes.OPEN_TASKFORCE_DIALOG:
            return args;
        default:
            return state;
    }
}

