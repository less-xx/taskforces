import React, { Component } from 'react';
import {
    Card, ListGroup, Modal
} from 'react-bootstrap';
import Moment from 'react-moment';
import DataService from '../DataService';
import TaskExecutionLog from '../taskforces/TaskExecutionLog';
import { tsThisType } from '@babel/types';

class TaskExecutionCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            taskforceId: props.taskforceId,
            taskExecution: null,
            showTaskExecLog: false,
            hasLastExec: false,
            isRunning: props.isRunning
        }
        this.monitor = null;
    }

    componentDidMount() {
        //this.queryTaskforceExecutions();
    }

    componentWillReceiveProps(props) {
        this.setState({
            isRunning: props.isRunning
        });
        this.queryTaskforceExecutions();
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

            var taskExecId = null;
            if (this.state.taskExecution) {
                taskExecId = this.state.taskExecution.id;
            }

            var execStatusCard = null;
            if (this.state.hasLastExec) {
                execStatusCard = <Card>
                    <Card.Body>
                        <Card.Title>Execution Status</Card.Title>
                        <Card.Text>Start: &nbsp;
                    <Moment format="MM-DD HH:mm">{new Date(this.state.taskExecution.startTime)}</Moment>
                        </Card.Text>
                        <Card.Text>End: &nbsp;{endTime} </Card.Text>
                        <ListGroup variant="flush" onClick={this.onClickItem.bind(this)} style={{ cursor: "pointer" }}>
                            {items}
                        </ListGroup>
                        <ListGroup variant="flush">
                            <Card.Link href="#" onClick={this.viewTaskExecLog.bind(this)}>View Log</Card.Link>
                        </ListGroup>
                    </Card.Body>
                </Card>
            } else {
                execStatusCard = <></>
            }

            return (
                <>
                    {execStatusCard}

                    <Modal size="lg"
                        scrollable
                        show={this.state.showTaskExecLog}
                        onHide={this.onCloseTaskExecLog.bind(this)}>
                        <Modal.Header closeButton>
                            <Modal.Title>Execution Logs</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <TaskExecutionLog taskExecId={taskExecId} />
                        </Modal.Body>
                    </Modal>
                </>
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

    queryTaskforceExecutions() {
        DataService.queryTaskforceExecution({
            taskforceId: this.state.taskforceId,
            size: 1,
            sort: ["startTime,desc"]
        }, (response) => {
            if (response.totalElements === 0) {
                console.log("empty executions");
                this.setState({ hasLastExec: false });
                //this.expandCollapseControlPanel(false);
            } else {
                this.setState({
                    hasLastExec: true,
                    taskExecution: response.content[0]
                });
                console.log(this.state.taskExecution);
            }
        },
            (error) => {
                console.log(error);
            });
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
                            console.log("task exec stopped, id = " + taskExecution.id);
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

    viewTaskExecLog() {
        this.setState({
            showTaskExecLog: true
        });
    }

    onCloseTaskExecLog() {
        this.setState({
            showTaskExecLog: false
        });
    }
}

export default TaskExecutionCard;