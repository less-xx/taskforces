import React, { Component } from 'react';
import {
    Card, ListGroup
} from 'react-bootstrap';

class TaskExecutionCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            progress: props.progress
        }
    }

    componentWillReceiveProps(props) {
        //console.log(props);
        this.setState({ progress: props.progress });
    }

    render() {
        if (this.state.progress) {

            var items = this.state.progress.map((p, index) => {
                return <ListGroup.Item eventKey={index} key={index}>{index}. {p.blockStatus}</ListGroup.Item>
            });
            return (

                <Card>
                    <Card.Header>Progress</Card.Header>
                    <ListGroup variant="flush" onClick={this.onClickItem.bind(this)}>
                        {items}
                    </ListGroup>
                </Card>
            );
        }
        return <div></div>;
    }

    onClickItem(evt) {
        var index = parseInt(evt.target.getAttribute("data-rb-event-key"));
        if (this.props.onClickItem) {
            this.props.onClickItem(this.state.progress[index])
        }
    }
}

export default TaskExecutionCard;