/**
 * 
 */
package org.teapotech.taskforce.web;

import org.teapotech.taskforce.entity.TaskforceEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author jiangl
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class SimpleTaskforceEntity {
	private String id;
	private String name;
	private String description;

	public SimpleTaskforceEntity() {
	}

	public SimpleTaskforceEntity(TaskforceEntity taskforce) {
		this.id = taskforce.getId();
		this.name = taskforce.getName();
		this.description = taskforce.getDescription();
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
}
