import React, { Component } from 'react';
import {
    Modal,
    ButtonToolbar,
    Button,
    Form
} from 'react-bootstrap';
import './Taskforces.css'
import Select from 'react-select';

class EditTaskforceModal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            show: props.show ? props.show : false,
            validated: false,
            groups: props.groups ? props.groups : [],
            taskforce: props.taskforce,
            taskforceId: null,
            taskforceName: null,
            taskforceDesc: null,
            taskforceGroupId: null,
            disableGroupSelection: props.disableGroupSelection
        };
    }

    componentWillReceiveProps(newProps) {
        console.log(newProps);
        this.setState({
            show: newProps.show ? newProps.show : false,
            groups: newProps.groups ? newProps.groups : []
        });
        if (newProps.taskforce) {
            this.setState({
                taskforceId: newProps.taskforce.id,
                taskforceName: newProps.taskforce.name,
                taskforceGroupId: newProps.taskforce.groupId,
                taskforceDesc: newProps.taskforce.description
            });
        }
    }

    render() {
        const groupOptions = this.state.groups.map(g =>
            ({
                value: g.id,
                label: g.name
            })
        );
        const title = this.state.taskforce ? "Edit Taskforce" : "New Taskfoce";
        const { validated } = this.state;
        return (
            <Modal show={this.state.show} onHide={this.hideNewTaskforce.bind(this)}>
                <Form noValidate validated={validated} onSubmit={e => this.handleSubmit(e)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        <Form.Group controlId="taskforceName">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter taskforce name" required onChange={this.onChangeName.bind(this)} defaultValue={this.state.taskforceName}/>
                            <Form.Text className="text-muted">
                                The name fo the taskforce should be unique.
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                Taskforce name is required.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="taskforceDescription">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea" rows="3" placeholder="Enter taskforce description" onChange={this.onChangeDesc.bind(this)} defaultValue={this.state.taskforceDesc} />
                        </Form.Group>

                        <Form.Group controlId="taskforceGroup">
                            <Form.Label>Group</Form.Label>
                            <Select options={groupOptions} defaultValue={groupOptions[0]} onChange={this.onChangeGroup.bind(this)} isDisabled={this.state.disabled} />
                        </Form.Group>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.hideNewTaskforce.bind(this)}>
                            Cancel
                        </Button>
                        <Button type="submit">
                            Save
                    </Button>
                    </Modal.Footer>
                </Form>
            </Modal >
        );
    }

    hideNewTaskforce() {
        this.setState({ show: false });
    }

    handleSubmit(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.stopPropagation();
            return;
        }
        this.setState({ validated: true });

        if (this.state.group) {
            this.updateTaskforce(
                (json) => {
                    console.log(json);
                    this.props.refresh();
                },
                (error) => {
                    console.log(error);
                });
        } else {
            this.createTaskforce(
                (json) => {
                    console.log(json);
                    this.props.refresh();
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    createTaskforce(success, failure) {
        var url = process.env.REACT_APP_URL_POST_TASKFORCE;
        fetch(url, {
            method: "POST",
            credentials: "include",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            },
            redirect: "follow",
            body: JSON.stringify({
                name: this.state.taskforceName,
                description: this.state.taskforceDesc,
                groupId: this.state.taskforceGroupId
            })
        })
            .then(response => response.json())
            .then(json => success(json))
            .then(this.setState({ isLoaded: true }))
            .catch(error => failure(error));
    }

    updateTaskforce(success, failure) {
        var url = process.env.REACT_APP_URL_PUT_TASKFORCE + "/" + this.state.taskforceId;
        fetch(url, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: this.state.taskforceName,
                description: this.state.taskforceDesc,
                groupId: this.state.taskforceGroupId
            })
        })
            .then(response => response.json())
            .then(json => success(json))
            .then(this.setState({ isLoaded: true }))
            .catch(error => failure(error));
    }

    onChangeName(e) {
        //console.log(e.target.value);
        this.setState({
            taskforceName: e.target.value
        });
    }

    onChangeDesc(e) {
        //console.log(e.target.value);
        this.setState({
            taskforceDesc: e.target.value
        });
    }

    onChangeGroup(e) {
        //console.log(e);
        this.setState({
            taskforceGroupId: e.value
        });
    }
}

export default EditTaskforceModal;