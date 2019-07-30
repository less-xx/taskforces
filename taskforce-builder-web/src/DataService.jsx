import DataStore from './DataStore';

const DataService = {

    fetchCustomBlockDefinitions: function (handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_GET_CUSTOM_BLOCK_DEFS;
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

    fetchTaskforceBlocks: function (handleBlocks, handleError) {
        var url = process.env.REACT_APP_URL_GET_TASKFORCE_BLOCKS;
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

    fetchTaskforceGroups: function (handleGroups, handleError) {
        var url = process.env.REACT_APP_URL_GET_TASKFORCE_GROUPS;
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var groups = result.body.content;
                    var pager = result.body.pageable;
                    DataStore.updateTaskforceGroups(groups, pager);
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

    fetchGroupTaskforces: function (groupId, handleTaskforces, handleError) {
        var url = new URL(process.env.REACT_APP_URL_GET_TASKFORCES);
        url.search = new URLSearchParams({
            group_id: groupId
        });
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var taskforces = result.body.content;
                    var pager = result.body.pageable;
                    DataStore.updateGroupTaskforces(groupId, taskforces, pager);
                    if (handleTaskforces) {
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
        var url = process.env.REACT_APP_URL_POST_TASKFORCE;
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
                DataStore.currentTaskforceId = json.body.id;
                if (handleResponse) {
                    handleResponse(json);
                }
            })
            .catch(error => handleError(error));
    },

    updateTaskforce: function (taskforceId, request, handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_PUT_TASKFORCE.replace(/\{.*\}/g, taskforceId);
        fetch(url, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        }).then(response => response.json())
            .then(json => {
                DataStore.currentTaskforceId = json.body.id;
                if (handleResponse) {
                    handleResponse(json);
                }
            })
            .catch(error => handleError(error));
    },

    fetchTaskforce: function (taskforceId, handleTaskforce, handleError) {
        var url = process.env.REACT_APP_URL_GET_TASKFORCE_BY_ID.replace(/\{.*\}/g, taskforceId);
        fetch(url)
            .then(res => res.json())
            .then(json => {
                DataStore.currentTaskforceId = json.body.id;
                if (handleTaskforce) {
                    handleTaskforce(json.body);
                }
            })
            .catch(error => handleError(error));
    },

    runTaskforce: function (taskforceId, handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_POST_EXECUTE_TASKFORCE;
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
                    handleResponse(json);
                }
            })
            .catch(error => handleError(error));
    },

    stopTaskforce: function (taskforceId, handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_POST_EXECUTE_TASKFORCE;
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
                    handleResponse(json);
                }
            })
            .catch(error => handleError(error));
    },

    queryTaskforceExecution: function (id, taskforceId, status, createdTime, createdBy,
        handleTaskforceExecution, handleError) {
        var url = new URL(process.env.REACT_APP_URL_GET_TASKFORCE_EXECUTION);
        var params = {};
        if (id != null) {
            params['id'] = id;
        }
        if (taskforceId != null) {
            params['taskforce_id'] = taskforceId;
        }
        if (status != null) {
            params['status'] = status;
        }
        if (createdTime != null) {
            params['created_time'] = createdTime;
        }
        if (createdBy != null) {
            params['created_by'] = createdBy;
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

    fetchFileSystemPaths: function (handlePaths, handleError) {
        var url = process.env.REACT_APP_URL_GET_CUSTOM_FILE_PATHS;
        fetch(url)
            .then(res => res.json())
            .then(json => {
                var paths = json.body.content;
                var pager = json.body.pageable;
                if (handlePaths) {
                    handlePaths(paths, pager);
                }
            })
            .catch(error => {
                if (handleError) {
                    handleError(error);
                }
            })
    },

    createFileSystemPath: function (request, handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_POST_CUSTOM_FILE_PATH;
        fetch(url, {
            method: "POST",
            credentials: "include",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            },
            redirect: "follow",
            body: JSON.stringify(request)
        }).then(response => {
            return response.json().then(body => {
                if (response.ok) {
                    handleResponse(body);
                } else {
                    handleError(body);
                }
            })
        }).catch(error => handleError(error));
    },

    updateFileSystemPath: function (pathId, request, handleResponse, handleError) {
        var url = process.env.REACT_APP_URL_PUT_CUSTOM_FILE_PATH.replace(/\{.*\}/g, pathId);
        fetch(url, {
            method: "PUT",
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
                    handleResponse(json);
                }
            })
            .catch(error => handleError(error));
    },

};
export default DataService;


