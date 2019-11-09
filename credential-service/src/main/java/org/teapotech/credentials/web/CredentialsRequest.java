/**
 * 
 */
package org.teapotech.credentials.web;

import org.teapotech.credentials.entity.Credentials.CredentialType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author lessdev
 *
 */
public class CredentialsRequest {

	private String name;
	private CredentialType type;
	private JsonNode configuration;

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

	public JsonNode getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JsonNode configuration) {
		this.configuration = configuration;
	}

}
