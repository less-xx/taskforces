import React, { Component } from 'react';
import {
    Card, ListGroup
} from 'react-bootstrap';
import DataService from '../DataService';

class TaskExecutionCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            taskExecution: null
        }
        this.monitor = null;
    }

    componentWillReceiveProps(props) {
        this.setState({
            taskExecution: props.taskExecution
        });
        if (this.state.taskExecution != null && this.state.taskExecution.status !== "Stopped") {
            if (this.monitor == null) {
                this.monitorTaskExec();
            }

        }
    }

    componentWillUpdate() {
        if (this.state.taskExecution != null && this.state.taskExecution.status === "Stopped") {
            if (this.monitor != null) {
                clearInterval(this.monitor);
            }
        }
    }

    render() {
        if (this.state.taskExecution) {
            const { progress } = this.state.taskExecution;

            var items = progress.map((p, index) => {
                return <ListGroup.Item eventKey={index} key={index}>{index}. {p.blockStatus}</ListGroup.Item>
            });
            return (

                <Card>
                    <Card.Body>
                        <Card.Title>Progress</Card.Title>
                        <ListGroup variant="flush" onClick={this.onClickItem.bind(this)} style={{ cursor: "pointer" }}>
                            {items}
                        </ListGroup>
                        <ListGroup variant="flush">
                            <Card.Link href="#" onClick={this.viewLog.bind(this)}>View Log</Card.Link>
                        </ListGroup>
                    </Card.Body>
                </Card>
            );
        }
        return <div></div>;
    }

    onClickItem(evt) {
        var index = parseInt(evt.target.getAttribute("data-rb-event-key"));
        if (this.props.onClickItem) {
            const { progress } = this.state.taskExecution;
            this.props.onClickItem(progress[index])
        }
    }

    monitorTaskExec() {
        if (this.monitor != null) {
            clearInterval(this.monitor);
            this.monitor = null;
        }
        if (this.state.taskExecution) {
            //console.log(this.state.taskExecution);
            const taskExecId = this.state.taskExecution.id;
            console.log("start monitoring task execution, id: " + taskExecId);
            this.monitor = setInterval(() => {
                console.log("query task execution, id: " + taskExecId);
                DataService.getTaskforceExecutionById(taskExecId,
                    (taskExecution) => {
                        console.log(taskExecution);
                        this.setState({
                            taskExecution: taskExecution
                        });
                    },
                    (error) => {
                        console.log(error);
                    });
            }, 5000);

        }

    }

    viewLog() {
        if (this.props.onViewLog) {
            this.props.onViewLog();
        }
    }
}

export default TaskExecutionCard;