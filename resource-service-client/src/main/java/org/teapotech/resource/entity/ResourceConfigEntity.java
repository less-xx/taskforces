/**
 * 
 */
package org.teapotech.resource.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "resource_config")
public class ResourceConfigEntity {

	public static enum Type {
		STATIC_OBJECT, STATIC_LIST, STATIC_MAP, STATIC_JSON, HTTP_RESOURCE, SQL_QUERY, SQL_UPDATE, CUSTOM
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "ds_config", columnDefinition = "TEXT", nullable = false)
	private String configuration;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "credential_ids", columnDefinition = "TEXT", nullable = true)
	private String credentialIds;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
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

	public String getCredentialIds() {
		return credentialIds;
	}

	public void setCredentialIds(String credentialIds) {
		this.credentialIds = credentialIds;
	}

	@Override
	public String toString() {
		return "{\n\tID: " + this.id + ",\n\tName: " + this.name + ",\n\tType: " + this.type + "\n}";
	}

}
