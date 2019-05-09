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
        console.log(props);
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
        const columns = [{
            dataField: 'name',
            text: 'Name',
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
            dataField: 'updatedBy',
            text: 'Updated By',
            align: 'center',
            headerAlign: 'center'
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
        this.props.parent.editTaskforce()
    }
}

export default GroupTaskforces;