
const DataStore = {

    taskforceGroups: {
        content: [],
        pager: null
    },

    groupTaskforce: [],

    currentTaskforceId: null,

    updateTaskforceGroups: function (groups, pager) {
        this.taskforceGroups.content.length = 0;
        this.taskforceGroups.content.push(...groups);
        this.taskforceGroups.pager = pager;
    },

    getGroupTaskforces: function (groupId) {
        //console.log("getGroupTaskforces(" + groupId + ")");
        var taskforces = this.groupTaskforce[groupId];
        if (taskforces) {
            return taskforces.content;
        }
        return [];
    },

    updateGroupTaskforces: function (groupId, taskforces, pager) {
        this.groupTaskforce[groupId] = {
            content: taskforces,
            pager: pager
        };
    },

    removeGroupTaskforces: function (groupId) {
        delete this.groupTaskforce[groupId];
    },

    setCurrentTaskforceId: function (id) {
        this.currentTaskforceId = id;
    }
}

export default DataStore;