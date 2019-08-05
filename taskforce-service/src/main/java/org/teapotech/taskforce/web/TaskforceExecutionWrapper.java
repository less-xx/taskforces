package org.teapotech.taskforce.web;

import java.util.Collection;
import java.util.Date;

import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;

public class TaskforceExecutionWrapper {

	private Long id;
	private SimpleTaskforceEntity taskforce;
	private String status;
	private Date createdTime;
	private String createdBy;
	private Date lastUpdatedTime;
	private Collection<BlockExecutionProgress> progress;

	public TaskforceExecutionWrapper(TaskforceExecution taskExec, Collection<BlockExecutionProgress> bep) {
		this.id = taskExec.getId();
		this.taskforce = taskExec.getTaskforce();
		this.status = taskExec.getStatus().name();
		this.createdTime = taskExec.getCreatedTime();
		this.createdBy = taskExec.getCreatedBy();
		this.lastUpdatedTime = taskExec.getLastUpdatedTime();
		this.progress = bep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SimpleTaskforceEntity getTaskforce() {
		return taskforce;
	}

	public void setTaskforce(SimpleTaskforceEntity taskforce) {
		this.taskforce = taskforce;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
