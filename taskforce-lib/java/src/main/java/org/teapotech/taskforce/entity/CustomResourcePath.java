/**
 * 
 */
package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "tf_cust_res_path")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "access_type", discriminatorType = DiscriminatorType.STRING)
public class CustomResourcePath {

	public static enum AccessType {
		File, SFTP, HTTP
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDHexGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

	public CustomResourcePath() {
	}

	public CustomResourcePath(String id, String name) {
		this.id = id;
		this.name = name;
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
