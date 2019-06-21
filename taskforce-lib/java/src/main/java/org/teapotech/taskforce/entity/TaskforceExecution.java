package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tf_task_execution")
public class TaskforceExecution {

	public static enum Status {
		Waiting, Running, Stopping, Stopped, Success, Failure
	}

	@Id
	@GeneratedValue(generator = "tf-exec-id-generator")
	@GenericGenerator(name = "tf-exec-id-generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "taskforce_id")
	private TaskforceEntity taskforce;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.Waiting;

	@Column(name = "created_time")
	@CreationTimestamp
	private Date createdTime;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Version
	@Column(name = "version", columnDefinition = "int DEFAULT 0", nullable = false)
	private int version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaskforceEntity getTaskforce() {
		return taskforce;
	}

	public void setTaskforce(TaskforceEntity taskforce) {
		this.taskforce = taskforce;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
