package org.teapotech.taskforce.web;

import org.teapotech.taskforce.entity.TaskforceGroup;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleTaskforceGroup {

	private String id;
	private String name;

	public SimpleTaskforceGroup() {
	}

	public SimpleTaskforceGroup(TaskforceGroup group) {
		this.id = group.getId();
		this.name = group.getName();
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

}
