export const TaskforceActionTypes = {
    RELOAD_GROUPS: "RELOAD_ALL_GROUPS"
}

export const reloadGroups = (groups, pager) => ({
    type: TaskforceActionTypes.RELOAD_GROUPS,
    groups,
    pager
})