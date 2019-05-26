import React, { Component } from 'react';
import {
    Modal,
    Button,
    Form
} from 'react-bootstrap';
import './Taskforces.css'

class EditTaskforceGroupModal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            group: props.group,
            show: false,
            validated: false,
            groupName: props.name,
            groupDesc: props.description,
            groupId: props.id,
        };
    }


    componentWillReceiveProps(newProps) {
        this.setState({
            show: newProps.show ? newProps.show : false,
            group: newProps.group
        });
        if (newProps.group) {
            this.setState({
                groupId: newProps.group.id,
                groupName: newProps.group.name,
                groupDesc: newProps.group.description,
            });
        }
    }

    render() {

        const { validated } = this.state;
        const title = this.state.group ? "Edit Taskforce Group" : "New Taskforce Group";
        return (
            <Modal show={this.state.show} onHide={this.hideEditGroup.bind(this)}>
                <Form noValidate validated={validated} onSubmit={e => this.handleSubmit(e)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form.Group controlId="groupName">
                            <Form.Label>Group Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter group name" required onChange={this.onChangeName.bind(this)} defaultValue={this.state.groupName} />
                            <Form.Text className="text-muted">
                                The name fo the group should be unique.
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                Group name is required.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="groupDescription">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea" rows="3" placeholder="Enter group description" onChange={this.onChangeDesc.bind(this)} defaultValue={this.state.groupDesc} />
                        </Form.Group>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.hideEditGroup.bind(this)}>
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

    hideEditGroup() {
        this.setState({ show: false });
    }

    handleSubmit(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.stopPropagation();
            this.setState({ validated: true });
            return;
        }
        this.setState({ validated: true });

        if (this.state.group) {
            this.updateTaskforceGroup(
                (json) => {
                    console.log(json);
                    this.props.refresh();
                },
                (error) => {
                    console.log(error);
                });
        } else {
            this.createTaskforceGroup(
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

    createTaskforceGroup(success, failure) {
        var url = process.env.REACT_APP_URL_POST_TASKFORCE_GROUP;
        fetch(url, {
            method: "POST",
            credentials: "include",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            },
            redirect: "follow",
            body: JSON.stringify({
                name: this.state.groupName,
                description: this.state.groupDesc
            })
        })
            .then(response => response.json())
            .then(json => success(json))
            .then(this.setState({ isLoaded: true }))
            .catch(error => failure(error));
    }

    updateTaskforceGroup(success, failure) {
        var url = process.env.REACT_APP_URL_PUT_TASKFORCE_GROUP + "/" + this.state.group.id;
        fetch(url, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: this.state.groupName,
                description: this.state.groupDesc
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
            groupName: e.target.value
        });
    }

    onChangeDesc(e) {
        //console.log(e.target.value);
        this.setState({
            groupDesc: e.target.value
        });
    }

}

export default EditTaskforceGroupModal;