/**
 * 
 */
package org.teapotech.block.docker;

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
	private final InputDescriptor inputDescriptor = new InputDescriptor();
	private final OutputDescriptor outputDescriptor = new OutputDescriptor();

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

	public InputDescriptor getInputDescriptor() {
		return inputDescriptor;
	}

	public OutputDescriptor getOutputDescriptor() {
		return outputDescriptor;
	}

	public static class InputDescriptor {
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	public static class OutputDescriptor {
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
