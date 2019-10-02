import { ActionTypes as TaskforceActions } from '../actions/TaskforceActions';

export const TaskforceReducer = (state = { groups: [] }, action) => {
    switch (action.type) {
        case TaskforceActions.RELOAD_GROUPS:
            let r=  Object.assign({}, state, {
                groups: action.groups
            })
            console.log(r);
            return r;
        default:
            return state;
    }
}