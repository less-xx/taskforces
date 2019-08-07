/**
 * 
 */
package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "v_taskforce_entity_execution")
@Immutable
public class SimpleTaskforceEntity {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "last_updated")
	private Date lastUpdatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	private SimpleTaskforceGroup group;

	@Column(name = "task_exec_id", length = 8)
	private Long executionId;

	@Column(name = "start_time")
	private Date execStartTime;

	@Column(name = "end_time")
	private Date execEndTime;

	@Column(name = "start_by")
	private String startBy;

	@Column(name = "end_by")
	private String endBy;

	@Column(name = "message")
	private String message;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private TaskforceExecution.Status execStatus;

	public SimpleTaskforceEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGroup(SimpleTaskforceGroup group) {
		this.group = group;
	}

	public SimpleTaskforceGroup getGroup() {
		return group;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	public Date getExecStartTime() {
		return execStartTime;
	}

	public void setExecStartTime(Date execStartTime) {
		this.execStartTime = execStartTime;
	}

	public Date getExecEndTime() {
		return execEndTime;
	}

	public void setExecEndTime(Date execEndTime) {
		this.execEndTime = execEndTime;
	}

	public TaskforceExecution.Status getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(TaskforceExecution.Status execStatus) {
		this.execStatus = execStatus;
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
