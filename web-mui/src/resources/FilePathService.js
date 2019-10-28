const URLS = {
    GET_CUSTOM_FILE_PATHS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/custom-file-paths",
    POST_CUSTOM_FILE_PATH: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/custom-file-paths",
    PUT_CUSTOM_FILE_PATH: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/custom-file-paths/{id}",
}

const FilePathService = {
    fetchFileSystemPaths: function (handlePaths, handleError) {
        const url = URLS.GET_CUSTOM_FILE_PATHS;
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
        const url = URLS.POST_CUSTOM_FILE_PATH;
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
        const url = URLS.PUT_CUSTOM_FILE_PATH.replace(/\{.*\}/g, pathId);
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
    }
}


export default FilePathService