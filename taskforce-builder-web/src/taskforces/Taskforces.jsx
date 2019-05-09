import React, { Component } from 'react';
import {
    Modal,
    Row,
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
            showEditGroupModal: false,
            showNewTaskforceModal: false,
            selectedGroup: null,
            selectTaskforce: null,
            showGroupOpMenu: false,
            groupOpMenuTarget: null
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

        var opColFormatter = function (cell, row, rowIndex, formatExtraData) {

            return (
                <MdMoreVert onClick={(e) => this.showTaskforceGroupMenu(e.target, row)} />
            );
        }.bind(this);

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
                <GroupTaskforces group={row} parent={parent} />
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
                    data={this.state.taskforceGroups}
                    columns={columns}
                    condensed
                    noDataIndication="No taskforce group"
                    wrapperClasses="taskforce-group-grid"
                    bordered={false}
                    expandRow={expandRow}
                />

                <EditTaskforceGroupModal group={this.state.selectedGroup} show={this.state.showEditGroupModal}
                    refresh={this.refresh.bind(this)} />

                <EditTaskforceModal groups={groups} taskforce={this.state.selectTaskforce}
                    show={this.state.showNewTaskforceModal} disableGroupSelection={true} />

                <Overlay target={this.state.groupOpMenuTarget} show={this.state.showGroupOpMenu} >
                    <Dropdown.Menu show>
                        <Dropdown.Item eventKey="1" onSelect={this.editGroup.bind(this)}>Edit</Dropdown.Item>
                        <Dropdown.Item eventKey="2">Delete</Dropdown.Item>
                    </Dropdown.Menu>
                </Overlay>

            </div>
        );
    }

    refresh() {
        this.setState({ showEditGroupModal: false });
        this.fetchGroups();
    }

    editTaskforceGroup() {
        this.setState({
            showEditGroupModal: true,
            showNewTaskforceModal: false
        });
    }

    editTaskforce(group) {
        this.setState({
            showNewTaskforceModal: true,
            showEditGroupModal: false,
            selectedGroup: group
        });
    }

    showTaskforceGroupMenu(target, group) {
        this.setState({
            groupOpMenuTarget: target,
            showGroupOpMenu: true,
            showEditGroupModal: false,
            selectedGroup: group
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