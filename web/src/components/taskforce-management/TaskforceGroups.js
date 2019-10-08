import React, { useState, useEffect, useRef } from 'react';
import ReactDataGrid from 'react-data-grid';
import { CustomFormat } from '../Format';
import Moment from 'react-moment';
import {
    Modal,
    Button,
    OverlayTrigger
} from 'react-bootstrap';
import { MdExpandLess, MdExpandMore, MdMoreVert } from 'react-icons/md';

function TaskforceGroups({ taskforceGroups, fetchTaskforceGroups }) {

    const [selectedGroupIndexes, setSelectedGroupIndexes] = useState([])
    console.log(selectedGroupIndexes)
    const [showGroupOpMenu, setShowGroupOpMenu] = useState(false)

    useEffect(() => {
        fetchTaskforceGroups();
    }, []);


    const dateTimeFormatter = ({ value }) => {
        return (
            <Moment format={CustomFormat.DATE_TIME_FORMAT}>
                {new Date(value)}
            </Moment>
        );
    };

    const onClickOpButton = (e, row) => {
        console.log(row);

        setShowGroupOpMenu(true)
    }

    const opColFormatter = ({ value }) => {
        return (   
            <OverlayTrigger
                placement="right-start"
                trigger={['click']}
                rootClose={true}
                overlay={TaskforceGroupOpMenu}
            >
                <MdMoreVert/>
            </OverlayTrigger>
            
        );
    };

    const onSelectTaskforceGroup = (rows)=>{
        console.log(rows)
        rows.forEach(r => {
            setSelectedGroupIndexes([r.rowIdx])
        });
    }

    const selectTaskforceGroup = (index) => {
        setSelectedGroupIndexes([index])
    }

    const deleteTaskforceGroup = (index) => {
        console.log(`Delete ${index}`)
        setSelectedGroupIndexes([index])
    }

    const editTaskforceGroup = (index) => {
        console.log(`Edit ${index}`)
        setSelectedGroupIndexes([index])
    }

    const columns = [
        { key: 'id', name: '', width:40, formatter: opColFormatter },
        { key: 'name', name: 'Name', resizable: "true", sortable: "true" },
        { key: 'description', name: 'Description', resizable: "true" },
        { key: 'lastUpdatedTime', name: 'Last Updated', formatter: dateTimeFormatter },
        { key: 'updatedBy', name: 'Updated By' }
    ];

    const rowSelection = {
        onRowsSelected: onSelectTaskforceGroup,
        showCheckbox: false,
        selectBy: {
            indexes: selectedGroupIndexes
        }
    }

    const rows = taskforceGroups || [];
    return (
        <div>
            <ReactDataGrid
                rowKey="id"
                columns={columns}
                rowGetter={i => rows[i]}
                rowsCount={rows.length} 
                rowSelection={rowSelection}
                onRowClick={selectTaskforceGroup}
            />

        </div>
    );
}

function TaskforceGroupOpMenu (props) {
    console.log(props)
    return (
        <div
        ref={props.ref}
        style={{
            backgroundColor: 'rgba(0, 0, 0, 0.85)',
            padding: '2px 10px',
            color: 'white',
            borderRadius: 3,
            ...props.style,
        }}
        >
        Simple tooltip
        </div>
    )
}

export default TaskforceGroups;