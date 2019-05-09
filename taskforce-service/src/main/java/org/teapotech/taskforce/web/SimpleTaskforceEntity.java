/**
 * 
 */
package org.teapotech.taskforce.web;

import java.util.Date;

import org.teapotech.taskforce.entity.TaskforceEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author jiangl
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleTaskforceEntity {
	private String id;
	private String name;
	private String description;
	private Date lastUpdatedTime;
	private String updatedBy;
	private SimpleTaskforceGroup group;

	public SimpleTaskforceEntity() {
	}

	public SimpleTaskforceEntity(TaskforceEntity taskforce) {
		this.id = taskforce.getId();
		this.name = taskforce.getName();
		this.description = taskforce.getDescription();
		this.lastUpdatedTime = taskforce.getLastUpdatedTime();
		this.updatedBy = taskforce.getUpdatedBy();
		this.group = new SimpleTaskforceGroup(taskforce.getGroup());
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
}
