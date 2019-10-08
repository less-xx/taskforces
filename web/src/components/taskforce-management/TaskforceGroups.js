import React, { useState, useEffect } from 'react';
import ReactDataGrid from 'react-data-grid';
import { CustomFormat } from '../Format';
import Moment from 'react-moment';
import {
    Modal,
    Button
} from 'react-bootstrap';
import { MdExpandLess, MdExpandMore, MdMoreVert } from 'react-icons/md';
import { Menu } from "react-data-grid-addons";

const { ContextMenu, MenuItem, ContextMenuTrigger } = Menu;

function TaskforceGroups({ taskforceGroups, fetchTaskforceGroups }) {

    const [selectedGroupIndexes, setSelectedGroupIndexes] = useState([])
    console.log(selectedGroupIndexes)

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

    const onClickOpButton = (row) => {
        console.log(row);
    }

    const opColFormatter = ({ value }) => {
        return (
            <MdMoreVert onClick={(e) => onClickOpButton(e.target, value)} />
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
                contextMenu={
                    <TaskforceGroupContextMenus
                      onRowDelete={(e, { rowIdx }) => deleteTaskforceGroup(rowIdx)}
                      onRowEdit={(e, { rowIdx }) => editTaskforceGroup(rowIdx)}
                    />
                }
                RowsContainer={ContextMenuTrigger}/>
        </div>
    );
}

function TaskforceGroupContextMenus({
        idx,
        id,
        rowIdx,
        onRowDelete,
        onRowEdit
    }) {
    return (
        <ContextMenu id={id}>
            <MenuItem data={{ rowIdx, idx }} onClick={onRowEdit}>
                Edit
            </MenuItem>
            <MenuItem data={{ rowIdx, idx }} onClick={onRowDelete}>
                Delete
              </MenuItem>  
        </ContextMenu>
      );
}
export default TaskforceGroups;