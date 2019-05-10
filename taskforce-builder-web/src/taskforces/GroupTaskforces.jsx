import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import { Link } from "react-router-dom";
import BootstrapTable from 'react-bootstrap-table-next';
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
            return (
                <Moment format="YYYY-MM-DD HH:mm">
                    {new Date(cell)}
                </Moment>
            );
        }

        var taskforceNameFormatter = function (cell, row) {
            return (
                <Link to="/taskforce-editor" onClick={DataStore.setCurrentTaskforceId(row.id)}> {cell} </Link>
            );
        }

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
                    noDataIndication = { <Button variant="outline-primary" size="sm" onClick={this.editTaskforce.bind(this)}> New Taskforce </Button> }
                    wrapperClasses="taskforce-grid"
                    bordered={false}
                />

            </div>
        );
    }

    editTaskforce() {
        console.log(this.state.taskforceGroup);
        this.props.parent.editTaskforce(this.state.taskforceGroup);
    }
}

export default GroupTaskforces;