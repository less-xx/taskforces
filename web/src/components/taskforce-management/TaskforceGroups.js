import React, { useState, useEffect } from 'react';
import ReactDataGrid from 'react-data-grid';
import { CustomFormat } from '../Format';
import Moment from 'react-moment';

function TaskforceGroups({ groups, fetchTaskforceGroups }) {

    console.log(groups)
    useEffect(() => {
        fetchTaskforceGroups();
    }, [groups]);

    const dateTimeFormatter = ({ value }) => {
        return (
            <Moment format={CustomFormat.DATE_TIME_FORMAT}>
                {new Date(value)}
            </Moment>
        );
    };

    const columns = [
        { key: 'name', name: 'Name' },
        { key: 'description', name: 'Description' },
        { key: 'lastUpdatedTime', name: 'Last Updated', formatter: dateTimeFormatter },
        { key: 'updatedBy', name: 'Updated By' }
    ];

    const rows = groups || [];
    console.log("row num: "+rows.length)
    return (
        <div>
            <ReactDataGrid
                columns={columns}
                rowGetter={i => rows[i]}
                rowsCount={rows.length} />
        </div>
    );
}

export default TaskforceGroups;