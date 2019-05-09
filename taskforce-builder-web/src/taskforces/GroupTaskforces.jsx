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
import './Taskforces.css'
import Moment from 'react-moment';
import EditTaskforceModal from './EditTaskforceModal';

class GroupTaskforces extends Component {

    constructor(props) {
        super(props);
        //console.log(props);
        this.state = {
            error: null,
            isLoaded: false,
            taskforceGroup: props.group,
            taskforces: [],
            pageable: {
                page: 0,
                size: 20,
                sort: null
            }
        };
    }

    componentDidMount() {
        this.loadGroupTaskforces();
    }
    
    loadGroupTaskforces() {
        var url = new URL(process.env.REACT_APP_URL_GET_TASKFORCES);
        url.search = new URLSearchParams({
            group_id: this.state.taskforceGroup.id
        });
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var taskforces = result.body.content;
                    var pager = result.body.pageable;
                    this.setState({
                        isLoaded: true,
                        taskforces: taskforces,
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

    render() {
        var dateTimeFormatter=function(cell, row) {
            return (
                <Moment format="YYYY-MM-DD HH:mm">
                    {new Date(cell)}
                </Moment>
            );
        }

        const columns = [{
            dataField: 'name',
            text: 'Name',
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
                    data={this.state.taskforces}
                    columns={columns}
                    stripped
                    hover
                    condensed
                    noDataIndication={<Button variant="outline-primary" size="sm" onClick={this.editTaskforce.bind(this)}>New Taskforce</Button>}
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