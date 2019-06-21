package org.teapotech.block.event;

import org.teapotech.taskforce.entity.TaskforceExecution.Status;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class WorkspaceExecutionEvent {

	public WorkspaceExecutionEvent() {
	}

	public WorkspaceExecutionEvent(String workspaceId, Status status) {
		this.workspaceId = workspaceId;
		this.status = status;
	}

	private String workspaceId;
	private Status status;

	public String getWorkspaceId() {
		return workspaceId;
	}

	protected void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
