import React, { Component } from 'react';
import {
    Card, ListGroup
} from 'react-bootstrap';
import Moment from 'react-moment';
import DataService from '../DataService';

class TaskExecutionCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            taskExecution: props.taskExecution
        }
        this.monitor = null;
    }

    componentWillReceiveProps(props) {
        console.log(props);
        this.setState({
            taskExecution: props.taskExecution
        });
    }

    render() {
        if (this.state.taskExecution) {
            const { progress } = this.state.taskExecution;

            var items = progress.map((p, index) => {
                return <ListGroup.Item eventKey={index} key={index}>Proc {index}: {p.blockStatus}</ListGroup.Item>
            });

            var endTime = null;
            if (this.state.taskExecution.status === "Stopped") {
                endTime = <Moment format="MM-DD HH:mm">{new Date(this.state.taskExecution.endTime)}</Moment>
                if (this.monitor != null) {
                    this.stopMonitorTaskExec();
                }
            } else {
                if (this.monitor == null) {
                    this.monitorTaskExec();
                }
            }

            return (

                <Card>
                    <Card.Body>
                        <Card.Title>Execution Status</Card.Title>
                        <Card.Text>Start: &nbsp;
                            <Moment format="MM-DD HH:mm">{new Date(this.state.taskExecution.startTime)}</Moment>
                        </Card.Text>
                        <Card.Text>End: &nbsp;
                            {endTime}
                        </Card.Text>
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
        return <></>;
    }

    onClickItem(evt) {
        var index = parseInt(evt.target.getAttribute("data-rb-event-key"));
        if (this.props.onClickItem) {
            const { progress } = this.state.taskExecution;
            this.props.onClickItem(progress[index])
        }
    }

    loadTaskExec(taskExecId) {
        const _this = this;
        DataService.getTaskforceExecutionById(taskExecId,
            (taskExecution) => {
                if (taskExecution.status === "Stopped") {
                    _this.stopMonitorTaskExec();
                    if (_this.props.onExecutionStopped) {
                        _this.props.onExecutionStopped();
                    }
                }
                this.setState({
                    taskExecution: taskExecution
                });
            },
            (error) => {
                console.log(error);
            });
    }

    monitorTaskExec() {

        const { taskExecution } = this.state;
        const _this = this;
        if (taskExecution) {

            console.log("start monitoring task execution, id: " + taskExecution.id);
            this.monitor = setInterval(() => {
                console.log("query task execution, id: " + taskExecution.id);
                DataService.getTaskforceExecutionById(taskExecution.id,
                    (taskExecution) => {
                        if (taskExecution.status === "Stopped") {
                            _this.stopMonitorTaskExec();
                            if (_this.props.onExecutionStopped) {
                                _this.props.onExecutionStopped();
                            }
                        }
                        this.setState({
                            taskExecution: taskExecution
                        });
                    },
                    (error) => {
                        console.log(error);
                    });
            }, 3000);

        }
    }

    stopMonitorTaskExec() {
        if (this.monitor != null) {
            clearInterval(this.monitor);
            this.monitor = null;
        }
    }

    viewLog() {
        if (this.props.onViewLog) {
            this.props.onViewLog();
        }
    }
}

export default TaskExecutionCard;