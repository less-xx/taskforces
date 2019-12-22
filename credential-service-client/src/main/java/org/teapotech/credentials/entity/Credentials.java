/**
 * 
 */
package org.teapotech.credentials.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "credentials")
public class Credentials implements Cloneable {

	public static enum CredentialType {
		USERNAME_PASSWORD, API_KEY, SSH_KEY, OAUTH, DB_CONNECTION
	}

	@Id
	@GeneratedValue(generator = "cred-id-generator")
	@GenericGenerator(name = "cred-id-generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT", nullable = true)
	private String description;

	@Column(name = "auth_method", nullable = false)
	@Enumerated(EnumType.STRING)
	private CredentialType type = CredentialType.USERNAME_PASSWORD;

	@Column(name = "credentials", columnDefinition = "TEXT", nullable = false)
	private String credentials;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "is_enabled", columnDefinition = "CHAR(1) DEFAULT 'Y'", nullable = false)
	@Type(type = "yes_no")
	private boolean enabled = true;

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

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "{\n\tID: " + this.id + ",\n\tName: " + this.name + ",\n\tType: " + this.type + "\n}";
	}
}
