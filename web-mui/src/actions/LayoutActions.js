export const LayoutActions = {
    TOGGLE_DRAWER: "TOGGLE_DRAWER"
}

export const toggleDrawer = (drawerOpen) => ({
    type: LayoutActions.TOGGLE_DRAWER,
    drawerOpen
})