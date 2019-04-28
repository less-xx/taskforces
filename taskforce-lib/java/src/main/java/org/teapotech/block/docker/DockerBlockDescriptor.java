/**
 * 
 */
package org.teapotech.block.docker;

import org.teapotech.block.executor.docker.DockerBlockValueType;

/**
 * @author jiangl
 *
 */
public class DockerBlockDescriptor {

	private String id;
	private String name;
	private String version;
	private String description;
	private boolean active;
	private String createdTime;
	private String category;
	private DockerBlockValueType inputValueType = DockerBlockValueType.TextPlain;
	private DockerBlockValueType outputValueType = DockerBlockValueType.TextPlain;
	private String definition;

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DockerBlockValueType getInputValueType() {
		return inputValueType;
	}

	public void setInputValueType(DockerBlockValueType inputValueType) {
		this.inputValueType = inputValueType;
	}

	public DockerBlockValueType getOutputValueType() {
		return outputValueType;
	}

	public void setOutputValueType(DockerBlockValueType outputValueType) {
		this.outputValueType = outputValueType;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
}
