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

    createTaskforceGroup: function(request, success, failure) {
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

    updateTaskforceGroup: function(groupId, request, success, failure) {
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

};

export default TaskforceService;