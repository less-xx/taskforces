const URLS = {
    GET_CREDENTIALS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/credentials",
    POST_CREDENTIALS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/credentials",
    GET_CREDENTIAL_BY_ID: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/credentials/{id}",
    PUT_CREDENTIALS: process.env.REACT_APP_SERVICE_BASE_URL + "/taskforce-service/credentials/{id}",
}

const UserCredentialService = {
    fetchCredentials: function (handleCredentials, handleError) {
        const url = URLS.GET_CREDENTIALS;
        fetch(url)
            .then(res => res.json())
            .then(json => {
                var credentials = json.body.content;
                var pager = json.body.pageable;
                if (handleCredentials) {
                    handleCredentials(credentials, pager);
                }
            })
            .catch(error => {
                if (handleError) {
                    handleError(error);
                }
            })
    },
    createCredentials: function (request, handleResponse, handleError) {
        const url = URLS.POST_CREDENTIALS;
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
    updateCredentials: function (credentialId, request, handleResponse, handleError) {
        const url = URLS.PUT_CREDENTIALS.replace(/\{.*\}/g, credentialId);
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


export default UserCredentialService