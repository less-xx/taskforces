/**
 * 
 */
package org.teapotech.credentials.util;

import java.io.IOException;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.teapotech.credentials.Oauth2Credentials;
import org.teapotech.credentials.Oauth2Credentials.AuthorizationType;
import org.teapotech.credentials.service.exception.InvalidOAuth2ConigurationException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author jiangl
 *
 */
public class OAuth2CredentialDeserializer extends StdDeserializer<Oauth2Credentials> {

	private static final long serialVersionUID = -2235177705067099446L;
	private final static String PROP_AUTHORIZATION_TYPE = "authorizationType";
	private final static String PROP_OAUTH2_RESOURCE = "oauth2Resource";

	public OAuth2CredentialDeserializer() {
		this(null);
	}

	protected OAuth2CredentialDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Oauth2Credentials deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode jn = p.getCodec().readTree(p);
		if (!(jn.has(PROP_AUTHORIZATION_TYPE) && jn.has(PROP_OAUTH2_RESOURCE))) {
			throw new InvalidOAuth2ConigurationException("Both authorizationType and oauth2Resource are required.");
		}
		Oauth2Credentials oauth2Cred = new Oauth2Credentials();
		String authorizationType = jn.get(PROP_AUTHORIZATION_TYPE).asText();
		JsonNode oauth2ResNode = jn.get(PROP_OAUTH2_RESOURCE);

		if (authorizationType.equalsIgnoreCase(AuthorizationType.AUTHORIZATION_CODE.name())) {
			AuthorizationCodeResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					AuthorizationCodeResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
			oauth2Cred.setAuthorizationType(AuthorizationType.AUTHORIZATION_CODE);
		} else if (authorizationType.equalsIgnoreCase(AuthorizationType.CLIENT_CREDENTIALS.name())) {
			ClientCredentialsResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					ClientCredentialsResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
			oauth2Cred.setAuthorizationType(AuthorizationType.CLIENT_CREDENTIALS);
		} else if (authorizationType.equalsIgnoreCase(AuthorizationType.IMPLICIT.name())) {
			ImplicitResourceDetails res = p.getCodec().treeToValue(oauth2ResNode, ImplicitResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
			oauth2Cred.setAuthorizationType(AuthorizationType.IMPLICIT);
		} else if (authorizationType.equalsIgnoreCase(AuthorizationType.PASSWORD.name())) {
			ResourceOwnerPasswordResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					ResourceOwnerPasswordResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
			oauth2Cred.setAuthorizationType(AuthorizationType.PASSWORD);
		} else {
			throw new InvalidOAuth2ConigurationException("Invalid authorization type " + authorizationType);
		}
		return oauth2Cred;
	}

}
