import { connect } from 'react-redux'

import TaskforceGroups from '../components/taskforce-management/TaskforceGroups'
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups } from '../actions/TaskforceActions';

const fetchTaskforceGroups = (dispatch) => {
    TaskforceService.fetchTaskforceGroups((groups, pager) => {
        console.log(groups);
        dispatch(reloadGroups(groups, pager));
    }, (error) => {
        console.log(error);
    });
}

const mapStateToProps = state => ({
    taskforceGroups: state.taskforceGroups
})

const mapDispatchToProps = dispatch => ({
    fetchTaskforceGroups: () => fetchTaskforceGroups(dispatch)
})



export default connect(mapStateToProps, mapDispatchToProps)(TaskforceGroups)