/**
 * 
 */
package org.teapotech.resource.web;

import java.util.Collection;

import org.teapotech.credentials.entity.Credentials;
import org.teapotech.resource.ResourceConfig;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.entity.ResourceConfigEntity;

/**
 * @author lessdev
 *
 */
public class ResourceConfigResponse {

	protected String id;
	private CredentialsWrapper credentials;
	private String name;
	private Collection<ResourceParameter<?>> boundParamteres;
	private Collection<ResourceParameter<?>> userParamteres;

	public ResourceConfigResponse(ResourceConfigEntity entity, ResourceConfig<?> resConfig, Credentials cred) {
		this.id = entity.getId();
		this.name = entity.getName();
		if (cred != null) {
			this.credentials = new CredentialsWrapper(cred.getId(), cred.getName());
		}
		this.boundParamteres = resConfig.getBoundParameters();
		this.userParamteres = resConfig.getUserParameters();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CredentialsWrapper getCredentials() {
		return credentials;
	}

	public void setCredentials(CredentialsWrapper credentials) {
		this.credentials = credentials;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<ResourceParameter<?>> getBoundParamteres() {
		return boundParamteres;
	}

	public void setBoundParamteres(Collection<ResourceParameter<?>> boundParamteres) {
		this.boundParamteres = boundParamteres;
	}

	public Collection<ResourceParameter<?>> getUserParamteres() {
		return userParamteres;
	}

	public void setUserParamteres(Collection<ResourceParameter<?>> userParamteres) {
		this.userParamteres = userParamteres;
	}

	public static class CredentialsWrapper {
		final String name;
		final String id;

		public CredentialsWrapper(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

}
