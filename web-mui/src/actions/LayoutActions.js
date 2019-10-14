export const LayoutActions = {
    TOGGLE_DRAWER: "TOGGLE_DRAWER",
    ACTIVE_NAVIATOR_MENU: "ACTIVE_NAVIATOR_MENU"
}

export const toggleDrawer = (drawerOpen) => ({
    type: LayoutActions.TOGGLE_DRAWER,
    drawerOpen
})

export const activeNavigatorMenu = (id) => ({
    type: LayoutActions.ACTIVE_NAVIATOR_MENU,
    id
})