import React, { Component } from 'react';
import {
    Dropdown,
    ButtonToolbar,
    Button,
    Overlay
} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import './Taskforces.css'
import Moment from 'react-moment';
import { MdExpandLess, MdExpandMore, MdMoreVert } from 'react-icons/md';
import EditTaskforceGroupModal from './EditTaskforceGroupModal';
import EditTaskforceModal from './EditTaskforceModal';
import GroupTaskforces from './GroupTaskforces';
import DataService from '../DataService';
import DataStore from '../DataStore';

class Taskforces extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            pageable: {
                page: 0,
                size: 20,
                sort: null
            },
            showEditGroupModal: false,
            showEditTaskforceModal: false,
            selectedGroup: null,
            selectedTaskforce: null,
            showGroupOpMenu: false,
            groupOpMenuTarget: null,
            taskforceOpMenuTarget: null,
            showTaskforceOpMenu: false
        };
    }

    componentDidMount() {
        this.setState({ isLoaded: false });
        DataService.fetchTaskforceGroups((groups, pager) => {
            this.setState({ isLoaded: true });
        });
    }

    render() {

        var opColFormatter = function (cell, row, rowIndex, formatExtraData) {

            return (
                <MdMoreVert onClick={(e) => this.showTaskforceGroupMenu(e.target, row)} />
            );
        }.bind(this);

        var dateTimeFormatter = function (cell, row) {
            return (
                <Moment format="YYYY-MM-DD HH:mm">
                    {new Date(cell)}
                </Moment>
            );
        }

        const columns = [{
            dataField: 'name',
            text: 'Group Name',
            align: 'left',
            headerAlign: 'left',
            headerStyle: { width: "20%" }
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
            headerStyle: { width: "150px" },
            formatter: dateTimeFormatter
        }, {
            dataField: 'id',
            text: '',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "40px" },
            formatter: opColFormatter
        }];
        var parent = this;
        const expandRow = {
            renderer: row => (
                <>
                    <GroupTaskforces group={row} parent={parent} />
                    <div style={{ marginBottom: 10, textAlign: "center" }}>
                        <Button variant="outline-primary" size="sm" onClick={(e) => this.newTaskforce(row)}> New Taskforce </Button>
                    </div>
                </>
            ),
            showExpandColumn: true,
            expandByColumnOnly: true,
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

        var groups = this.state.selectedGroup ? [this.state.selectedGroup] : this.state.taskforceGroups;
        return (
            <div>
                <h1>Taskforces</h1>
                <ButtonToolbar className="major-operation-button-bar">
                    <Button variant="outline-primary" size="sm" onClick={this.editTaskforceGroup.bind(this)}>New Group</Button>
                </ButtonToolbar>

                <BootstrapTable
                    keyField='id'
                    data={DataStore.taskforceGroups.content}
                    columns={columns}
                    condensed
                    noDataIndication="No taskforce group"
                    wrapperClasses="taskforce-group-grid"
                    bordered={false}
                    expandRow={expandRow}
                />

                <EditTaskforceGroupModal group={this.state.selectedGroup} show={this.state.showEditGroupModal}
                    refresh={this.refresh.bind(this)} />

                <EditTaskforceModal groups={groups} taskforce={this.state.selectedTaskforce} group={this.state.selectedGroup}
                    refresh={this.refreshGroupTaskforces.bind(this)}
                    show={this.state.showEditTaskforceModal} disableGroupSelection={this.state.selectedTaskforce == null} />

                <Overlay target={this.state.groupOpMenuTarget} show={this.state.showGroupOpMenu} >
                    <Dropdown.Menu show>
                        <Dropdown.Item eventKey="1" onSelect={this.editGroup.bind(this)}>Edit</Dropdown.Item>
                        <Dropdown.Item eventKey="2">Delete</Dropdown.Item>
                    </Dropdown.Menu>
                </Overlay>

                <Overlay target={this.state.taskforceOpMenuTarget} show={this.state.showTaskforceOpMenu} >
                    <Dropdown.Menu show>
                        <Dropdown.Item eventKey="1" onSelect={this.editTaskforce.bind(this)}>Edit</Dropdown.Item>
                        <Dropdown.Item eventKey="2">Delete</Dropdown.Item>
                    </Dropdown.Menu>
                </Overlay>

            </div>
        );
    }

    refresh() {
        this.setState({
            showEditGroupModal: false,
            isLoaded: false
        });
        DataService.fetchTaskforceGroups((groups, pager) => {
            this.setState({ isLoaded: true });
        });
    }

    refreshGroupTaskforces() {
        this.setState({
            showEditTaskforceModal: false,
            isLoaded: false
        });
        DataService.fetchGroupTaskforces(this.state.selectedGroup.id, (taskforces, pager) => {
            this.setState({ isLoaded: true });
        });
    }

    editTaskforceGroup() {
        this.setState({
            showEditGroupModal: true,
            showEditTaskforceModal: false,
            selectedGroup: null
        });
    }

    editTaskforce() {
        this.setState({
            showEditTaskforceModal: true,
            showEditGroupModal: false,
            showTaskforceOpMenu: false,
            selectedGroup: this.state.selectedGroup,
            selectedTaskforce: this.state.selectedTaskforce
        });
    }

    newTaskforce(group) {
        console.log(group);
        this.setState({
            showEditTaskforceModal: true,
            showEditGroupModal: false,
            showTaskforceOpMenu: false,
            selectedGroup: group,
            selectedTaskforce: null
        });
    }

    showTaskforceGroupMenu(target, group) {
        this.setState({
            groupOpMenuTarget: target,
            showGroupOpMenu: true,
            showEditGroupModal: false,
            showEditTaskforceModal: false,
            selectedGroup: group
        });
        //console.log(target);
    }

    showTaskforceContextMenu(target, taskforce) {
        this.setState({
            taskforceOpMenuTarget: target,
            showGroupOpMenu: false,
            showTaskforceOpMenu: true,
            showEditGroupModal: false,
            showEditTaskforceModal: false,
            selectedTaskforce: taskforce
        });
        //console.log(target);
    }

    editGroup() {
        //console.log(this.state.selectedGroup);
        this.setState({
            showGroupOpMenu: false
        });
        this.editTaskforceGroup();
    }
}

export default Taskforces;