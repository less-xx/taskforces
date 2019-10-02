export const ActionTypes = {
    RELOAD_GROUPS: "RELOAD_ALL_GROUPS"
}

export const reloadGroups = (groups, pager) => ({
    type: ActionTypes.RELOAD_GROUPS,
    groups,
    pager
})