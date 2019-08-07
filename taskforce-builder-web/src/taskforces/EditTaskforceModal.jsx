import React, { Component } from 'react';
import {
    Modal,
    Button,
    Form
} from 'react-bootstrap';
import './Taskforces.css'
import Select from 'react-select';
import DataService from '../DataService';
import DataStore from '../DataStore';

class EditTaskforceModal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            show: props.show ? props.show : false,
            validated: false,
            taskforce: props.taskforce,
            taskforceId: null,
            taskforceName: null,
            taskforceDesc: null,
            taskforceGroupId: props.group ? props.group.id : null,
            disableGroupSelection: props.disableGroupSelection
        };
    }

    componentWillReceiveProps(newProps) {
        //console.log(newProps);
        this.setState({
            show: newProps.show ? newProps.show : false,
            groups: newProps.groups ? newProps.groups : [],
            disableGroupSelection: newProps.disableGroupSelection
        });
        if (newProps.taskforce) {
            this.setState({
                taskforceId: newProps.taskforce.id,
                taskforceName: newProps.taskforce.name,
                taskforceGroupId: newProps.taskforce.group.id,
                taskforceDesc: newProps.taskforce.description
            });
        } else if (newProps.group) {
            this.setState({
                taskforceGroupId: newProps.group.id
            });
        }
    }

    render() {
        const groupOptions = DataStore.taskforceGroups.content.map(g =>
            ({
                value: g.id,
                label: g.name
            })
        );
        const tgid = this.state.taskforceGroupId;
        //console.log(tgid);
        const defaultGroup = groupOptions.filter(g => g.value === tgid);
        
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
                            <Form.Control type="text" placeholder="Enter taskforce name" required onChange={this.onChangeName.bind(this)} defaultValue={this.state.taskforceName} />
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
                            <Select options={groupOptions} defaultValue={groupOptions[0]} onChange={this.onChangeGroup.bind(this)} isDisabled={this.state.disableGroupSelection} />
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

        if (this.state.taskforce) {
            var request1 = {
                name: this.state.taskforceName,
                description: this.state.taskforceDesc,
                groupId: this.state.taskforceGroupId
            };
            DataService.updateTaskforce(this.state.taskforceId, request1,
                (json) => {
                    console.log(json);
                    this.props.refresh();
                },
                (error) => {
                    console.log(error);
                });
        } else {
            var request2 = {
                name: this.state.taskforceName,
                description: this.state.taskforceDesc,
                groupId: this.state.taskforceGroupId,
                configuration: "<xml></xml>"
            };
            DataService.createTaskforce(request2,
                (json) => {
                    console.log(json);
                    this.props.refresh();
                },
                (error) => {
                    console.log(error);
                });
        }
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