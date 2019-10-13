const testReducer = (state = 'origin', action) => {
    switch (action.type) {
        case "TEST_ACTION":
            return action.value;
        default:
            return state
    }
}
export default testReducer;