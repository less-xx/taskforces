/**
 * 
 */
package org.teapotech.taskforce.entity;

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
@Table(name = "tf_credentials")
public class Credentials {

	public static enum Catalog {
		USERNAME_PASSWORD, DATABASE_CONNECTION_JDBC, WS_OAUTH2, WS_API_KEY
	}

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDHexGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "catalog", nullable = false)
	@Enumerated(EnumType.STRING)
	private Catalog catalog;

	@Column(name = "credentials", columnDefinition = "TEXT", nullable = false)
	private String credentials;

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

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
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
		return "{\n\tID: " + this.id
				+ ",\n\tName: " + this.name
				+ ",\n\tCalalog: " + this.catalog
				+ "\n}";
	}
}
