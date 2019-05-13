/**
 * 
 */
package org.teapotech.taskforce.credential.oauth2;

import java.io.IOException;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.teapotech.taskforce.credential.Oauth2Credentials;

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

	private static final long serialVersionUID = 8859934990032123595L;

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

		if (authorizationType.equals(Oauth2Credentials.TYPE_AUTHORIZATION_CODE)) {
			AuthorizationCodeResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					AuthorizationCodeResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
		} else if (authorizationType.equals(Oauth2Credentials.TYPE_CLIENT_CREDENTIALS)) {
			ClientCredentialsResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					ClientCredentialsResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
		} else if (authorizationType.equals(Oauth2Credentials.TYPE_IMPLICIT)) {
			ImplicitResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					ImplicitResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
		} else if (authorizationType.equals(Oauth2Credentials.TYPE_RESOURCE_OWNER_PASSWORD)) {
			ResourceOwnerPasswordResourceDetails res = p.getCodec().treeToValue(oauth2ResNode,
					ResourceOwnerPasswordResourceDetails.class);
			oauth2Cred.setOauth2Resource(res);
		} else {
			throw new InvalidOAuth2ConigurationException("Invalid authorization type " + authorizationType);
		}
		oauth2Cred.setAuthorizationType(authorizationType);
		return oauth2Cred;
	}

}
