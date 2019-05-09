/**
 * 
 */
package org.teapotech.taskforce.web;

import javax.validation.constraints.NotNull;

/**
 * @author jiangl
 *
 */
public class TaskforceGroupRequest {

	@NotNull
	private String name;
	private String description;

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
