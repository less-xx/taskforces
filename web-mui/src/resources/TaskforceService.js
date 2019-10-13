const URLS = {

    GET_TASKFORCE_GROUPS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups",
    POST_TASKFORCE_GROUP: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups",
    PUT_TASKFORCE_GROUP: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-groups/{id}"

}

const TaskforceService = {

    fetchTaskforceGroups: function (handleGroups, handleError) {
        var url = URLS.GET_TASKFORCE_GROUPS;
        fetch(url)
            .then(res => res.json())
            .then(
                (result) => {
                    var groups = result.body.content;
                    var pager = result.body.pageable;
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
    }

};

export default TaskforceService;