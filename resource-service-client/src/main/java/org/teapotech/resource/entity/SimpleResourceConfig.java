/**
 * 
 */
package org.teapotech.resource.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "resource_config")
@Immutable
public class SimpleResourceConfig {

	@Id
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ResourceConfigEntity.Type type;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

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

	public ResourceConfigEntity.Type getType() {
		return type;
	}

	public void setType(ResourceConfigEntity.Type type) {
		this.type = type;
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

	@Override
	public String toString() {
		return "{\n\tID: " + this.id + ",\n\tName: " + this.name + ",\n\tType: " + this.type + "\n}";
	}

}
