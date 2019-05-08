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
import Select from 'react-select';

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
            showNewTaskforceModal: false
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
        const groupOptions = this.state.taskforceGroups.map(g=>
            ({
                value: g.id, 
                label: g.name
            })
        );
        return (
            <div>
                <h1>Taskforces</h1>
                <ButtonToolbar className="major-operation-button-bar">
                    <Button variant="outline-primary" size="sm" onClick={this.showNewTaskforce.bind(this)}>New</Button>
                </ButtonToolbar>

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

                <Modal show={this.state.showNewTaskforceModal} onHide={this.hideNewTaskforce.bind(this)}>
                    <Modal.Header closeButton>
                        <Modal.Title>New Taskforce</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form noValidate validated={validated} onSubmit={e => this.createTaskforce(e)}>
                            <Form.Group controlId="taskforceName">
                                <Form.Label>Taskforce Name</Form.Label>
                                <Form.Control type="text" placeholder="Enter taskforce name" required/>
                                <Form.Text className="text-muted">
                                    The name fo the taskforce should be unique.
                                </Form.Text>
                                <Form.Control.Feedback type="invalid">
                                    Taskforce name is required.
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="taskforceGroup">
                                <Form.Label>Group</Form.Label>
                                <Select options={groupOptions} />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.hideNewTaskforce.bind(this)}>
                            Cancel
                        </Button>
                        <Button variant="primary" type="submit">
                            Create
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        );
    }

    hideNewTaskforce() {
        this.setState({ showNewTaskforceModal: false });
    }

    showNewTaskforce() {
        this.setState({ showNewTaskforceModal: true });
    }

    createTaskforce() {

    }
}

export default Taskforces;