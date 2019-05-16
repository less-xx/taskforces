/**
 * 
 */
package org.teapotech.taskforce.web;

/**
 * @author jiangl
 *
 */
public class FileSystemPathRequest {

	private String name;
	private String description;
	private String path;
	private boolean createIfNotExist;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCreateIfNotExist() {
		return createIfNotExist;
	}

	public void setCreateIfNotExist(boolean createIfNotExist) {
		this.createIfNotExist = createIfNotExist;
	}

}
