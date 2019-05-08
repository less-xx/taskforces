/**
 * 
 */
package org.teapotech.taskforce.web;

import javax.validation.constraints.NotNull;

/**
 * @author jiangl
 *
 */
public class TaskforceRequest {

	@NotNull
	private String name;
	private String description;
	private String groupId;

	@NotNull
	private String configuration;

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

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
