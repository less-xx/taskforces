package org.teapotech.block.event;

import org.teapotech.taskforce.entity.TaskforceExecution.Status;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class WorkspaceExecutionEvent {
	private String workspaceId;
	private Status status;
	private String taskforceId;
	private Long taskforceExecutionId;

	public WorkspaceExecutionEvent() {
	}

	public WorkspaceExecutionEvent(String workspaceId, Status status) {
		this.workspaceId = workspaceId;
		this.status = status;
		parseWorkspaceId(workspaceId);

	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	protected void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
		parseWorkspaceId(workspaceId);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getTaskforceExecutionId() {
		return taskforceExecutionId;
	}

	public String getTaskforceId() {
		return taskforceId;
	}

	private void parseWorkspaceId(String workspaceId) {
		String[] ss = workspaceId.split("_");
		this.taskforceId = ss[0];
		this.taskforceExecutionId = new Long(ss[1]);
	}
}
