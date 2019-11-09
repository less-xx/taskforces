/**
 * 
 */
package org.teapotech.credentials.web;

import java.util.Date;

import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.CredentialType;

/**
 * @author lessdev
 *
 */
public class SimpleCredentialsResponse {

	private String id;
	private String name;
	private CredentialType type;
	private Date lastUpdatedTime;
	private String updatedBy;
	private boolean enabled = true;

	public SimpleCredentialsResponse(Credentials cred) {
		this.id = cred.getId();
		this.name = cred.getName();
		this.type = cred.getType();
		this.lastUpdatedTime = cred.getLastUpdatedTime();
		this.updatedBy = cred.getUpdatedBy();
		this.enabled = cred.isEnabled();
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

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
