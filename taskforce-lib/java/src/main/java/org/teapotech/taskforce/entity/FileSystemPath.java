/**
 * 
 */
package org.teapotech.taskforce.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author lessdev
 *
 */
@Entity
@DiscriminatorValue("File")
public class FileSystemPath extends CustomResourcePath {

	@Column(name = "file_path", columnDefinition = "TEXT", nullable = false)
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
