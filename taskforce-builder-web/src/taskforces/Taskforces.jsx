import React, { Component } from 'react';
import {
    Modal,
    Row,
    Col,
    ButtonToolbar,
    Button,
    Form
} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import './Taskforces.css'
import Moment from 'react-moment';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import EditTaskforceGroupModal from './EditTaskforceGroupModal';
import EditTaskforceModal from './EditTaskforceModal';
import GroupTaskforces from './GroupTaskforces';
import { padding } from 'polished';

class Taskforces extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            taskforceGroups: [],
            pageable: {
                page: 0,
                size: 20,
                sort: null
            },
            showNewGroupModal: false,
            showNewTaskforceModal: false,
            selectedGroup: null,
            selectTaskforce: null
        };
    }

    componentDidMount() {
        this.fetchGroups();
    }

    fetchGroups() {
        var url = process.env.REACT_APP_URL_GET_TASKFORCE_GROUPS;
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var groups = result.body.content;
                    var pager = result.body.pageable;
                    this.setState({
                        isLoaded: true,
                        taskforceGroups: groups,
                        pageable: {
                            page: pager.number,
                            size: pager.size
                        }
                    });
                },
                (error) => {
                    console.log(error);
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );
    }

    dateTimeFormatter(cell, row) {
        return (
            <Moment format="YYYY-MM-DD HH:mm">
                {new Date(cell)}
            </Moment>
        );
    }

    render() {
        const columns = [{
            dataField: 'name',
            text: 'Group Name',
            align: 'left',
            headerAlign: 'left',
            headerStyle: { width: "15%" }
        }, {
            dataField: 'description',
            text: 'Description',
            align: 'left',
            headerAlign: 'left',
        }, {
            dataField: 'lastUpdatedTime',
            text: 'Last Updated',
            align: 'center',
            headerAlign: 'center',
            formatter: this.dateTimeFormatter
        }];
        var parent = this;
        const expandRow = {
            renderer: row => (
                <GroupTaskforces group={row} parent={parent}/>
            ),
            showExpandColumn: true,
            expandHeaderColumnRenderer: ({ isAnyExpands }) => {
                return "";
            },
            expandColumnRenderer: ({ expanded }) => {
                if (expanded) {
                    return <MdExpandLess />;
                }
                return <MdExpandMore />;
            }
        };

        var groups = this.state.selectedGroup?[this.state.selectedGroup]:[];
        return (
            <div>
                <h1>Taskforces</h1>
                <ButtonToolbar className="major-operation-button-bar">
                    <Button variant="outline-primary" size="sm" onClick={this.newTaskforceGroup.bind(this)}>New Group</Button>
                </ButtonToolbar>

                <BootstrapTable
                    keyField='id'
                    data={this.state.taskforceGroups}
                    columns={columns}
                    hover
                    condensed
                    noDataIndication="No taskforce group"
                    wrapperClasses="taskforce-group-grid"
                    bordered={false}
                    expandRow={expandRow}
                />

                <EditTaskforceGroupModal group={this.state.selectedGroup} show={this.state.showNewGroupModal}
                    refresh={this.refresh.bind(this)} />

                <EditTaskforceModal groups={groups} taskforce={this.state.selectTaskforce}
                    show={this.state.showNewTaskforceModal} disableGroupSelection={true} />
            </div>
        );
    }

    refresh() {
        this.setState({ showNewGroupModal: false });
        this.fetchGroups();
    }

    newTaskforceGroup() {
        this.setState({ showNewGroupModal: true });
    }

    editTaskforce() {
        this.setState({ showNewTaskforceModal: true });
    }
}

export default Taskforces;