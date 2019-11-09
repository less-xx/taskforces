/**
 * 
 */
package org.teapotech.resource.web;

import java.util.Map;

/**
 * @author lessdev
 *
 */
public class SQLQueryResourceRequest extends ResourceConfigRequest {

	private String credentialsId;
	private Map<String, Object> boundParamteres;
	private Map<String, Object> userParamteres;

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

}
