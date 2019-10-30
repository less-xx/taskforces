package org.teapotech.credentials;

import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;

public class Oauth2Credentials extends CredentialsObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4974649194832665591L;

	public static enum AuthorizationType {
		CLIENT_CREDENTIALS, PASSWORD, AUTHORIZATION_CODE, IMPLICIT
	}

	private AuthorizationType authorizationType;
	private BaseOAuth2ProtectedResourceDetails oauth2Resource;

	public AuthorizationType getAuthorizationType() {
		return authorizationType;
	}

	public void setAuthorizationType(AuthorizationType authorizationType) {
		this.authorizationType = authorizationType;
	}

	public BaseOAuth2ProtectedResourceDetails getOauth2Resource() {
		return oauth2Resource;
	}

	public void setOauth2Resource(BaseOAuth2ProtectedResourceDetails oauth2Resource) {
		this.oauth2Resource = oauth2Resource;
	}
}
