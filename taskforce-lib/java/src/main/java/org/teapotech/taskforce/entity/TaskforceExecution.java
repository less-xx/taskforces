package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tf_task_execution")
public class TaskforceExecution {

	public static enum Status {
		Waiting, Running, Stopping, Stopped, Success, Failure
	}

	@Id
	@SequenceGenerator(name = "task_execution_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_execution_id_seq")
	@Column(name = "id", columnDefinition = "serial")
	private Long id;

	@Column(name = "taskforce_id", nullable = false)
	private String taskforceId;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.Waiting;

	@Column(name = "start_time", nullable = true)
	@CreationTimestamp
	private Date startTime;

	@Column(name = "end_time", nullable = true)
	private Date endTime;

	@Column(name = "start_by", nullable = true)
	private String startBy;

	@Column(name = "end_by", nullable = true)
	private String endBy;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Version
	@Column(name = "version", columnDefinition = "int DEFAULT 0", nullable = false)
	private int version;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
