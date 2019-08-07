package org.teapotech.taskforce.web;

import java.util.Collection;
import java.util.Date;

import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.taskforce.entity.TaskforceExecution;

public class TaskforceExecutionWrapper {

	private Long id;
	private String taskforceId;
	private String status;
	private Date startTime;
	private Date endTime;
	private String startBy;
	private String endBy;
	private Date lastUpdatedTime;
	private Collection<BlockExecutionProgress> progress;

	public TaskforceExecutionWrapper(TaskforceExecution taskExec, Collection<BlockExecutionProgress> bep) {
		this.id = taskExec.getId();
		this.taskforceId = taskExec.getTaskforceId();
		this.status = taskExec.getStatus().name();
		this.startTime = taskExec.getStartTime();
		this.endTime = taskExec.getEndTime();
		this.startBy = taskExec.getStartBy();
		this.endBy = taskExec.getEndBy();
		this.lastUpdatedTime = taskExec.getLastUpdatedTime();
		this.progress = bep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskforceId() {
		return taskforceId;
	}

	public void setTaskforceId(String taskforceId) {
		this.taskforceId = taskforceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStartBy() {
		return startBy;
	}

	public void setStartBy(String startBy) {
		this.startBy = startBy;
	}

	public String getEndBy() {
		return endBy;
	}

	public void setEndBy(String endBy) {
		this.endBy = endBy;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void setProgress(Collection<BlockExecutionProgress> progress) {
		this.progress = progress;
	}

	public Collection<BlockExecutionProgress> getProgress() {
		return progress;
	}
}
