/**
 * 
 */
package org.teapotech.resource.web;

import java.util.Map;

import org.teapotech.resource.entity.ResourceConfigEntity;

/**
 * @author lessdev
 *
 */
public abstract class ResourceConfigRequest {

	protected String name;
	private String credentialsId;
	private ResourceConfigEntity.Type type;
	private Map<String, Object> boundParamteres;
	private Map<String, Object> userParamteres;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCredentialsId() {
		return credentialsId;
	}

	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}

	public Map<String, Object> getBoundParamteres() {
		return boundParamteres;
	}

	public void setBoundParamteres(Map<String, Object> boundParamteres) {
		this.boundParamteres = boundParamteres;
	}

	public Map<String, Object> getUserParamteres() {
		return userParamteres;
	}

	public void setUserParamteres(Map<String, Object> userParamteres) {
		this.userParamteres = userParamteres;
	}

	public ResourceConfigEntity.Type getType() {
		return type;
	}

	public void setType(ResourceConfigEntity.Type type) {
		this.type = type;
	}

}
