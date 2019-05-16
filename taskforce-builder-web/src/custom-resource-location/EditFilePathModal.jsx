import React, { Component } from 'react';
import {
    Modal,
    Button,
    Form
} from 'react-bootstrap';
import DataService from '../DataService';

class EditFilePathModal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            show: false,
            validated: false,
            name: props.name,
            description: props.description,
            id: props.id,
            path: props.path,
            createOnNotExist: false
        };
    }


    componentWillReceiveProps(newProps) {
        this.setState({
            show: newProps.show ? newProps.show : false,
        });
        if (newProps.filePath) {
            this.setState({
                id: newProps.filePath.id,
                name: newProps.filePath.name,
                path: newProps.filePath.path,
                description: newProps.filePath.description,
            });
        }
    }

    render() {

        const { validated } = this.state;
        const title = this.state.id ? "Edit File Path" : "New File Path";
        return (
            <Modal show={this.state.show} onHide={this.hideModal.bind(this)}>
                <Form noValidate validated={validated} onSubmit={e => this.handleSubmit(e)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form.Group controlId="filePathName">
                            <Form.Label>Group Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter the name" required onChange={this.onChangeName.bind(this)} defaultValue={this.state.name} />
                            <Form.Text className="text-muted">
                                The name should be unique.
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                Name is required.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="filePathValue">
                            <Form.Label>Path</Form.Label>
                            <Form.Control type="text" placeholder="Enter the file/folder path" required onChange={this.onChangePath.bind(this)} defaultValue={this.state.path} />
                            <Form.Control.Feedback type="invalid">
                                File/Folder path is required.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="createOnNotExist">
                            <Form.Check type="checkbox" onChange={this.onChangeCreateOnNotExist.bind(this)} label="Create if not exist" />
                        </Form.Group>

                        <Form.Group controlId="groupDescription">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea" rows="3" placeholder="Enter group description" onChange={this.onChangeDesc.bind(this)} defaultValue={this.state.description} />
                        </Form.Group>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.hideModal.bind(this)}>
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

    hideModal() {
        this.setState({ show: false });
    }

    handleSubmit(event) {
        
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.stopPropagation();
            event.preventDefault();
            this.setState({ validated: true });
            return;
        }
        
        event.preventDefault();

        var request = {
            name: this.state.name,
            path: this.state.path,
            description: this.state.description,
            createIfNotExist: this.state.createOnNotExist   
        }

        if(this.state.id){
            DataService.updateFileSystemPath(this.state.id, request, (resp)=>{
                this.hideModal();
                console.log(resp);
                this.props.refresh();
            }, (error)=>{
                console.log(error);
            });
        }else{    
            DataService.createFileSystemPath(request, (resp)=>{
                this.hideModal();
                console.log(resp);
                this.props.refresh();
            }, (error)=>{
                console.log(error);
            });
        }
        

    }

    onChangeName(e) {
        //console.log(e.target.value);
        this.setState({
            name: e.target.value
        });
    }

    onChangeDesc(e) {
        //console.log(e.target.value);
        this.setState({
            description: e.target.value
        });
    }

    onChangePath(e) {
        //console.log(e.target.value);
        this.setState({
            path: e.target.value
        });
    }

    onChangeCreateOnNotExist(e){
        this.setState({
            createOnNotExist: e.target.checked
        });
    }

}

export default EditFilePathModal;