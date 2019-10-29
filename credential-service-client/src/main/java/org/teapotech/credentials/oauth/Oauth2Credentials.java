package org.teapotech.credentials.oauth;

import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.teapotech.credentials.CredentialsObject;

public class Oauth2Credentials extends CredentialsObject {
	private static final long serialVersionUID = 7311826211465663285L;

	public static final String TYPE_CLIENT_CREDENTIALS = "client_credentials";
	public static final String TYPE_RESOURCE_OWNER_PASSWORD = "password";
	public static final String TYPE_AUTHORIZATION_CODE = "authorization_code";
	public static final String TYPE_IMPLICIT = "implicit";

	private String authorizationType;
	private BaseOAuth2ProtectedResourceDetails oauth2Resource;

	public String getAuthorizationType() {
		return authorizationType;
	}

	public void setAuthorizationType(String authorizationType) {
		this.authorizationType = authorizationType;
	}

	public BaseOAuth2ProtectedResourceDetails getOauth2Resource() {
		return oauth2Resource;
	}

	public void setOauth2Resource(BaseOAuth2ProtectedResourceDetails oauth2Resource) {
		this.oauth2Resource = oauth2Resource;
	}
}
