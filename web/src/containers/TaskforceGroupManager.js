import { connect } from 'react-redux'
import TaskforceService from '../resources/TaskforceService';
import { reloadGroups } from '../actions/TaskforceActions';

import TaskforceGroups from '../components/taskforce-management/TaskforceGroups'

const fetchTaskforceGroups = (dispatch) => {
    TaskforceService.fetchTaskforceGroups((groups, pager) => {
        console.log(groups);
        dispatch(reloadGroups(groups, pager));
    }, (error) => {
        console.log(error);
    });
}

const mapStateToProps = state => ({
    groups: state.groups
})

const mapDispatchToProps = dispatch => ({
    fetchTaskforceGroups: () => fetchTaskforceGroups(dispatch)
})



export default connect(mapStateToProps, mapDispatchToProps)(TaskforceGroups)