import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import DataService from '../DataService';

class TaskforceExecutionLog extends Component {

    constructor(props) {
        super(props)
        this.state = {
            taskExecId: props.taskExecId,
            start: 0,
            lines: 100,
            logContent: "",
            hasMore: true
        }
    }

    componentDidMount() {
        if (this.state.taskExecId) {
            this.fetchLog();
        }

    }

    render() {
        return (
            <>
                <pre>
                    {this.state.logContent}
                </pre>
                {this.state.hasMore ? <Button variant="link" onClick={this.fetchLog.bind(this)}>Load More …</Button> : <></>}
            </>
        );
    }

    fetchLog() {
        const { logContent, taskExecId, start, lines } = this.state;
        if (taskExecId == null) {
            return;
        }
        DataService.getTaskforceExecutionLogsById(taskExecId, start, lines,
            (logs) => {
                if (logs === "") {
                    this.setState({
                        hasMore: false
                    });
                } else {
                    var lineNumber = logs.split('\n').length;
                    this.setState({
                        logContent: logContent + "\n" + logs,
                        start: start + lines
                    });
                    if (lineNumber < 100) {
                        this.setState({
                            hasMore: false
                        });
                    }
                }

            },
            (error) => {
                console.log(error);
            });
    }
}

export default TaskforceExecutionLog;