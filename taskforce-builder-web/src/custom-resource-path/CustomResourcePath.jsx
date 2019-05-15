import React, { Component } from 'react';
import {
    Dropdown,
    ButtonToolbar,
    Button,
    Overlay
} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import { MdExpandLess, MdExpandMore, MdMoreVert } from 'react-icons/md';
import DataService from '../DataService';
import DataStore from '../DataStore';
class CustomResourcePath extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            pageable: {
                page: 0,
                size: 20,
                sort: null
            },
            userDefinedFileSystemPaths: null,
            showEditFilePathModal: false
        };
    }

    componentDidMount() {
        this.setState({ isLoaded: false });
        DataService.fetchFileSystemPaths((paths, pager) => {
            this.setState({ 
                isLoaded: true,
                pager: pager,
                userDefinedFileSystemPaths: paths
            });
            
        });
    }

    editUserDefinedFilePath() {
        this.setState({
            showEditFilePathModal: true
        });
    }

    render() {
        var opColFormatter = function (cell, row, rowIndex, formatExtraData) {

            return (
                <MdMoreVert onClick={(e) => this.showTaskforceGroupMenu(e.target, row)} />
            );
        }.bind(this);

        var dateTimeFormatter = function (cell, row) {
            return (
                <Moment format="YYYY-MM-DD HH:mm">
                    {new Date(cell)}
                </Moment>
            );
        }

        const columns = [{
            dataField: 'name',
            text: 'Name',
            align: 'left',
            headerAlign: 'left',
            headerStyle: { width: "20%" }
        }, {
            dataField: 'description',
            text: 'Description',
            align: 'left',
            headerAlign: 'left',
        }, {
            dataField: 'path',
            text: 'Path',
            align: 'left',
            headerAlign: 'left',
        },{
            dataField: 'lastUpdatedTime',
            text: 'Last Updated',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "150px" },
            formatter: dateTimeFormatter
        }, {
            dataField: 'id',
            text: '',
            align: 'center',
            headerAlign: 'center',
            headerStyle: { width: "40px" },
            formatter: opColFormatter
        }];

       return (
            <div>
                <h1>User Defined File System Path</h1>
                <ButtonToolbar className="major-operation-button-bar">
                    <Button variant="outline-primary" size="sm" onClick={this.editUserDefinedFilePath.bind(this)}>New Path</Button>
                </ButtonToolbar>

                <BootstrapTable
                    keyField='id'
                    data={this.state.userDefinedFileSystemPaths}
                    columns={columns}
                    condensed
                    noDataIndication="No user defined paths"
                    wrapperClasses="taskforce-group-grid"
                    bordered={false}
                />

                <Overlay target={this.state.groupOpMenuTarget} show={this.state.showGroupOpMenu} >
                    <Dropdown.Menu show>
                        <Dropdown.Item eventKey="1" onSelect={this.editGroup.bind(this)}>Edit</Dropdown.Item>
                        <Dropdown.Item eventKey="2">Delete</Dropdown.Item>
                    </Dropdown.Menu>
                </Overlay>

            </div>
        );
    }
}

export default CustomResourcePath;