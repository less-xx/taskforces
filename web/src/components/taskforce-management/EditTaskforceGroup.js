import React, { useState } from 'react';
import {
    Form
} from 'react-bootstrap';

const EditTaskforceGroup = (taskforceGroup, submitAction) =>{

    const [validated, setValidated] = useState(false)
    const groupName = taskforceGroup ? taskforceGroup.name : '';
    const groupDesc = taskforceGroup ? taskforceGroup.description : '';

    return (
        <Form noValidate validated={validated} onSubmit={e => submitAction(e)}>    
            <Form.Group controlId="groupName">
                <Form.Label>Group Name</Form.Label>
                <Form.Control type="text" placeholder="Enter group name" required defaultValue={groupName} />
                <Form.Text className="text-muted">
                    The name fo the group should be unique.
                </Form.Text>
                <Form.Control.Feedback type="invalid">
                    Group name is required.
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group controlId="groupDescription">
                <Form.Label>Description</Form.Label>
                <Form.Control as="textarea" rows="3" placeholder="Enter group description" defaultValue={groupDesc} />
            </Form.Group>
        </Form>
    );

}

export default EditTaskforceGroup