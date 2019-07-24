package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@SequenceGenerator(name = "task_execution_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "task_execution_id")
	@Column(name = "id", columnDefinition = "serial")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "taskforce_id")
	private SimpleTaskforceEntity taskforce;

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
