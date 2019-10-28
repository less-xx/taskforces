export const ResourceActionTypes = {
    OPEN_RESOURCE_DIALOG: "OPEN_RESOURCE_DIALOG",
}

export const ResourceDialogTypes = {
    EDIT_USER_DEFINED_FILE_PATH: "EDIT_USER_DEFINED_FILE_PATH",
}

export const openResourceDialog = (dialogType, isOpen, data) => ({
    type: ResourceActionTypes.OPEN_RESOURCE_DIALOG,
    dialog: dialogType,
    open: isOpen,
    data: data
})
