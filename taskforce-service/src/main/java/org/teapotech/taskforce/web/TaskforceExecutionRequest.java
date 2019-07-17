package org.teapotech.taskforce.web;

public class TaskforceExecutionRequest {

	public static enum Action {
		start, stop
	}

	private String taskforceId;
	private Action action;

	public String getTaskforceId() {
		return taskforceId;
	}

	public void setTaskforceId(String taskforceId) {
		this.taskforceId = taskforceId;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
