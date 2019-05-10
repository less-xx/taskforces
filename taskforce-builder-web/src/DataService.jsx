import DataStore from './DataStore';

const DataService = {

    fetchTaskforceBlocks: function (handleBlocks, handleError) {
        var url = process.env.REACT_APP_URL_GET_TASKFORCE_BLOCKS;
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    handleBlocks(result);
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

};
export default DataService;


