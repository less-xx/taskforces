export const UserCredentialsActionTypes = {
    OPEN_CREDENTIALS_DIALOG: "OPEN_CREDENTIALS_DIALOG",
}

export const UserCredentialsDialogTypes = {
    EDIT_USER_CREDENTIALS: "EDIT_USER_CREDENTIALS",
}

export const openUserCredentialsDialog = (dialogType, isOpen, data) => ({
    type: UserCredentialsActionTypes.OPEN_CREDENTIALS_DIALOG,
    dialog: dialogType,
    open: isOpen,
    data: data
})