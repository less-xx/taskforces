import React, { Component } from 'react';
import {
    Container,
    Row,
    Col,
    ButtonToolbar,
    Button
} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import './Taskforces.css'
import Moment from 'react-moment';

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
            }
        };
    }

    componentDidMount() {
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
        //console.log(cell);
        //return "aa";
        //return new Date(cell).toISOString();
        return (
            <Moment format="YYYY-MM-DD HH:mm">
                {new Date(cell)}
            </Moment>
        );
    }

    render() {
        const columns = [{
            dataField: 'name',
            text: 'Name',
            headerStyle: { width: "15%" }
        }, {
            dataField: 'description',
            text: 'Description'
        }, {
            dataField: 'lastUpdatedTime',
            text: 'Last Updated',
            formatter: this.dateTimeFormatter
        }];
        return (
            <div>
                <h1>Taskforces</h1>
                <ButtonToolbar className="major-operation-button-bar">
                    <Button variant="outline-primary" size="sm" href="/taskforce-editor">New</Button>
                </ButtonToolbar>;
                <BootstrapTable
                    keyField='id'
                    data={this.state.taskforceGroups}
                    columns={columns}
                    striped
                    hover
                    condensed
                    noDataIndication="Table is Empty"
                    wrapperClasses="taskforce-grid"
                    bordered={false}
                />
            </div>
        );
    }
}

export default Taskforces;