const URLS = {

    GET_CUSTOM_BLOCK_DEFS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/custom-block-definitions",
    GET_TASKFORCE_BLOCKS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/taskforce-blocks"

}

export const BlockService = {

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

}