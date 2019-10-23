export const TaskforceActionTypes = {
    RELOAD_GROUPS: "RELOAD_ALL_GROUPS",
    OPEN_TASKFORCE_DIALOG: "OPEN_TASKFORCE_DIALOG",
    TASKFORCE_OPERATION: "OPEN_TASKFORCE_DIALOG",
    LOAD_GROUP_TASKFORCES: "LOAD_GROUP_TASKFORCES",
}

export const TaskforceDialogTypes = {
    EDIT_TASKFORCE_GROUP: "EDIT_TASKFORCE_GROUP",
    EDIT_TASKFORCE: "EDIT_TASKFORCE",
}

export const reloadGroups = (groups, pager) => ({
    type: TaskforceActionTypes.RELOAD_GROUPS,
    groups,
    pager
})

export const loadGroupTaskforces = (taskforces) => ({
    type: TaskforceActionTypes.LOAD_GROUP_TASKFORCES,
    taskforces
})

export const openTaskforceDialog = (dialogType, isOpen, data) => ({
    type: TaskforceActionTypes.OPEN_TASKFORCE_DIALOG,
    dialog: dialogType,
    open: isOpen,
    data: data
})
