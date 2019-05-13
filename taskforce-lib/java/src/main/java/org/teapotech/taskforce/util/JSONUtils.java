/**
 * 
 */
package org.teapotech.taskforce.util;

import java.io.IOException;
import java.io.InputStream;

import org.teapotech.taskforce.credential.Oauth2Credentials;
import org.teapotech.taskforce.credential.oauth2.OAuth2CredentialDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jiangl
 *
 */
public class JSONUtils {

	private final static SimpleModule module = new SimpleModule();
	static {
		module.addDeserializer(Oauth2Credentials.class, new OAuth2CredentialDeserializer());
	}
	public final static ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false)
			.setSerializationInclusion(Include.NON_NULL)
			.registerModule(module);

	public static String getJSON(Object o) throws JsonProcessingException {
		if (o == null) {
			return null;
		}
		return mapper.writeValueAsString(o);
	}

	public static <T> T getObject(String json, Class<T> clazz)
			throws IOException {
		if (json == null) {
			return null;
		}
		return mapper.readValue(json, clazz);
	}

	public static <T> T getObject(InputStream in, Class<T> clazz)
			throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readValue(in, clazz);
	}

	public static <T> T getObject(JsonNode node, Class<T> clazz) {
		if (node == null) {
			return null;
		}
		return mapper.convertValue(node, clazz);
	}

	@SuppressWarnings("rawtypes")
	public static <T> T getObject(InputStream in, TypeReference valueTypeRef)
			throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readValue(in, valueTypeRef);
	}

	@SuppressWarnings("rawtypes")
	public static <T> T getObject(String json, TypeReference valueTypeRef)
			throws IOException {
		if (json == null) {
			return null;
		}
		return mapper.readValue(json, valueTypeRef);
	}

	public static JsonNode getObject(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readTree(in);
	}

	public static JsonNode getObject(String jsonStr) throws IOException {
		if (jsonStr == null) {
			return null;
		}
		return mapper.readTree(jsonStr);
	}

	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}

	public static ArrayNode createArrayNode() {
		return mapper.createArrayNode();
	}

}
