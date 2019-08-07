import React, { Component } from 'react';
import {
    Badge
} from 'react-bootstrap';
import { Link } from "react-router-dom";
import BootstrapTable from 'react-bootstrap-table-next';
import { MdMoreVert } from 'react-icons/md';
import './Taskforces.css'
import Moment from 'react-moment';
import DataService from '../DataService';
import DataStore from '../DataStore';

class GroupTaskforces extends Component {

    constructor(props) {
        super(props);
        console.log(props);
        this.state = {
            error: null,
            isLoaded: false,
            taskforceGroup: props.group,
            pageable: {
                page: 0,
                size: 20,
                sort: null
            }
        };
    }

    componentDidMount() {
        this.setState({
            isLoaded: false
        });
        DataService.fetchGroupTaskforces(this.state.taskforceGroup.id, (taskforce, pager) => {
            this.setState({
                isLoaded: true
            });
        });
    }

    render() {
        var dateTimeFormatter = function (cell, row) {
            if (cell == null) {
                return <></>
            }
            return (
                <Moment format="YYYY-MM-DD HH:mm">
                    {new Date(cell)}
                </Moment>
            );
        }

        var execTimeFormatter = function (cell, row) {
            if (cell == null) {
                return <></>
            }
            return (
                <Moment format="YYYY-MM-DD HH:mm:ss">
                    {new Date(cell)}
                </Moment>
            );
        }

        var taskforceNameFormatter = function (cell, row) {
            return (
                <Link to="/taskforce-editor" onClick={DataStore.setCurrentTaskforceId(row.id)}> {cell} </Link>
            );
        }

        var execStatusFormatter = function (cell, row) {
            if (cell === "Running") {
                return <Badge variant="success">{cell}</Badge>
            } else if (cell === "Stopped") {
                return <Badge variant="secondary">{cell}</Badge>
            }
            return <Badge variant="warning">{cell}</Badge>
        }

        var opColFormatter = function (cell, row, rowIndex, formatExtraData) {

            return (
                <MdMoreVert onClick={(e) => this.editTaskforce(e, row)} />
            );
        }.bind(this);

        const columns = [{
            dataField: 'name',
            text: 'Name',
            align: 'left',
            headerAlign: 'left',
            headerStyle: { width: "20%" },
            formatter: taskforceNameFormatter
        }, {
            dataField: 'description',
            text: 'Description',
            align: 'left',
            headerAlign: 'left',
        }, {
            dataField: 'execStatus',
            text: 'Execution Status',
            align: 'center',
            headerAlign: 'center',
            formatter: execStatusFormatter
        }, {
            dataField: 'execStartTime',
            text: 'Start Time',
            align: 'center',
            headerAlign: 'center',
            formatter: execTimeFormatter
        }, {
            dataField: 'execEndTime',
            text: 'End Time',
            align: 'center',
            headerAlign: 'center',
            formatter: execTimeFormatter
        }, {
            dataField: 'lastUpdatedTime',
            text: 'Last Updated',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "150px" },
            formatter: dateTimeFormatter
        }, {
            dataField: 'updatedBy',
            text: 'Updated By',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "150px" }
        }, {
            dataField: 'id',
            text: '',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "40px" },
            formatter: opColFormatter
        }];
        return (
            <div>
                <BootstrapTable
                    keyField='id'
                    data={DataStore.getGroupTaskforces(this.state.taskforceGroup.id)}
                    columns={columns}
                    stripped
                    hover
                    condensed
                    wrapperClasses="taskforce-grid"
                    bordered={false}
                />

            </div>
        );
    }

    editTaskforce(evt, taskforce) {
        console.log(evt.target);
        this.props.parent.showTaskforceContextMenu(evt.target, taskforce);
    }
}

export default GroupTaskforces;