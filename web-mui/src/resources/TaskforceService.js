const URLS = {

    GET_TASKFORCE_GROUPS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups",
    GET_TASKFORCE_GROUP_BY_ID: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups/{id}",
    POST_TASKFORCE_GROUP: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups",
    PUT_TASKFORCE_GROUP: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups/{id}",
    GET_TASKFORCES: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforces",
    POST_TASKFORCE: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforces",
    PUT_TASKFORCE: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforces/{id}",
    GET_TASKFORCE_BY_ID: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforces/{id}",
    GET_TASKFORCE_BLOCKS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-blocks",
    GET_CUSTOM_BLOCK_DEFS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/custom-block-definitions",

    POST_EXECUTE_TASKFORCE: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-executions",
    GET_TASKFORCE_EXECUTION: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-executions",
    GET_TASKFORCE_EXECUTION_BY_ID: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-executions/{id}",
    GET_TASKFORCE_EXECUTION_LOG_BY_ID: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-executions/{id}/logs",
}

const TaskforceService = {

    fetchTaskforceGroups: function (handleGroups, handleError) {
        var url = new URL(URLS.GET_TASKFORCE_GROUPS);
        url.search = new URLSearchParams({
            sort: "lastUpdatedTime,desc"
        });
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    const groups = result.body.content;
                    const pager = result.body.pageable;
                    if (handleGroups) {
                        handleGroups(groups, pager);
                    }
                }
            )
            .catch(error => {
                console.log(error);
                if (handleError) {
                    handleError(error);
                }
            });
    },

    fetchTaskforceGroupById: function (groupId, handleGroup, handleError) {
        const url = URLS.GET_TASKFORCE_GROUP_BY_ID.replace(/\{.*\}/g, groupId);
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    const group = result.body;
                    if (handleGroup) {
                        handleGroup(group);
                    }
                }
            )
            .catch(error => {
                console.log(error);
                if (handleError) {
                    handleError(error);
                }
            });
    },

    createTaskforceGroup: function (request, success, failure) {
        const url = URLS.POST_TASKFORCE_GROUP;
        fetch(url, {
                method: "POST",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            })
            .then(response => response.json())
            .then(json => success(json))
            .catch(error => failure(error));
    },

    updateTaskforceGroup: function (groupId, request, success, failure) {
        var url = URLS.PUT_TASKFORCE_GROUP.replace(/\{.*\}/g, groupId);
        fetch(url, {
                method: "PUT",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            })
            .then(response => response.json())
            .then(json => success(json))
            .catch(error => failure(error));
    },

    fetchTaskforceBlocks: function (handleBlocks, handleError) {
        var url = URLS.GET_TASKFORCE_BLOCKS;
        fetch(url)
            .then(response => response.text())
            .then(str => (new window.DOMParser()).parseFromString(str, "text/xml"))
            .then(
                (result) => {
                    handleBlocks(result.documentElement);
                })
            .catch(error => {
                handleError(error);
            });
    },


    fetchGroupTaskforces: function (groupId, handleTaskforces, handleError) {
        var url = new URL(URLS.GET_TASKFORCES);
        url.search = new URLSearchParams({
            group_id: groupId
        });
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var taskforces = result.body.content;
                    var pager = result.body.pageable;
                    if (handleTaskforces) {
                        //console.log(taskforces);
                        handleTaskforces(taskforces, pager);
                    }
                }
            )
            .catch(error => {
                console.log(error);
                if (handleError) {
                    handleError(error);
                }
            });
    },

    createTaskforce: function (request, handleResponse, handleError) {
        var url = URLS.POST_TASKFORCE;
        fetch(url, {
                method: "POST",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json"
                },
                redirect: "follow",
                body: JSON.stringify(request)
            }).then(response => response.json())
            .then(json => {
                if (handleResponse) {
                    handleResponse(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    updateTaskforce: function (taskforceId, request, handleResponse, handleError) {
        var url = URLS.PUT_TASKFORCE.replace(/\{.*\}/g, taskforceId);
        fetch(url, {
                method: "PUT",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            }).then(response => response.json())
            .then(json => {
                if (handleResponse) {
                    handleResponse(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    fetchTaskforce: function (taskforceId, handleTaskforce, handleError) {
        var url = URLS.GET_TASKFORCE_BY_ID.replace(/\{.*\}/g, taskforceId);
        fetch(url)
            .then(res => res.json())
            .then(json => {
                if (handleTaskforce) {
                    handleTaskforce(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    fetchCustomBlockDefinitions: function (handleResponse, handleError) {
        var url = URLS.GET_CUSTOM_BLOCK_DEFS;
        fetch(url)
            .then(res => {
                return res.json();
            })
            .then(
                (result) => {
                    handleResponse(result);
                })
            .catch(error => {
                handleError(error);
            });
    },
    runTaskforce: function (taskforceId, handleResponse, handleError) {
        var url = URLS.POST_EXECUTE_TASKFORCE;
        fetch(url, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "taskforceId": taskforceId,
                    "action": "start"
                })
            }).then(response => response.json())
            .then(json => {
                if (handleResponse) {
                    handleResponse(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    stopTaskforce: function (taskforceId, handleResponse, handleError) {
        var url = URLS.POST_EXECUTE_TASKFORCE;
        fetch(url, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "taskforceId": taskforceId,
                    "action": "stop"
                })
            }).then(response => response.json())
            .then(json => {
                if (handleResponse) {
                    handleResponse(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    queryTaskforceExecution: function (request,
        handleTaskforceExecution, handleError) {
        var url = new URL(URLS.GET_TASKFORCE_EXECUTION);
        var params = {};
        if (request.id != null) {
            params['id'] = request.id;
        }
        if (request.taskforceId != null) {
            params['taskforce_id'] = request.taskforceId;
        }
        if (request.status != null) {
            params['status'] = request.status;
        }
        if (request.createdTime != null) {
            params['created_time'] = request.createdTime;
        }
        if (request.createdBy != null) {
            params['created_by'] = request.createdBy;
        }
        if (request.sort != null) {
            params['sort'] = request.sort;
        }
        if (request.page != null) {
            params['page'] = request.page;
        }
        if (request.size != null) {
            params['size'] = request.size;
        }

        url.search = new URLSearchParams(params)
        fetch(url)
            .then(res => res.json())
            .then(json => {
                if (handleTaskforceExecution) {
                    handleTaskforceExecution(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    getTaskforceExecutionById: function (taskExecId, handleResponse, handleError) {
        var url = URLS.GET_TASKFORCE_EXECUTION_BY_ID.replace(/\{.*\}/g, taskExecId);
        fetch(url)
            .then(res => res.json())
            .then(json => {
                if (handleResponse) {
                    handleResponse(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    getTaskforceExecutionLogsById: function (taskExecId, start, lines, handleResponse, handleError) {
        var url = new URL(URLS.GET_TASKFORCE_EXECUTION_LOG_BY_ID.replace(/\{.*\}/g, taskExecId));
        if (start === null) {
            start = 0;
        }
        if (lines === null) {
            lines = 100;
        }
        var params = {
            "start": start,
            "lines": lines
        };
        url.search = new URLSearchParams(params)
        fetch(url)
            .then(res => res.text())
            .then(text => {
                if (handleResponse) {
                    handleResponse(text);
                }
            })
            .catch(error => handleError(error));
    },
};

export default TaskforceService;