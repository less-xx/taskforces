/**
 * 
 */
package org.teapotech.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jiangl
 *
 */
public class JsonHelper {

	private final SimpleModule module = new SimpleModule();
	ObjectMapper mapper = null;

	public <T> JsonHelper addDeserializer(Class<T> type, JsonDeserializer<? extends T> deser) {
		module.addDeserializer(type, deser);
		return this;
	}

	private JsonHelper() {
	}

	public static JsonHelper newInstance() {
		return new JsonHelper();
	}

	public JsonHelper build() {
		mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setSerializationInclusion(Include.NON_NULL).registerModule(module);
		return this;
	}

	public String getJSON(Object o) throws JsonProcessingException {
		if (o == null) {
			return null;
		}
		return mapper.writeValueAsString(o);
	}

	public <T> T getObject(String json, Class<T> clazz) throws IOException {
		if (json == null) {
			return null;
		}
		return mapper.readValue(json, clazz);
	}

	public <T> T getObject(InputStream in, Class<T> clazz) throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readValue(in, clazz);
	}

	public <T> T getObject(JsonNode node, Class<T> clazz) {
		if (node == null) {
			return null;
		}
		return mapper.convertValue(node, clazz);
	}

	@SuppressWarnings("rawtypes")
	public <T> T getObject(InputStream in, TypeReference valueTypeRef) throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readValue(in, valueTypeRef);
	}

	@SuppressWarnings("rawtypes")
	public <T> T getObject(String json, TypeReference valueTypeRef) throws IOException {
		if (json == null) {
			return null;
		}
		return mapper.readValue(json, valueTypeRef);
	}

	public JsonNode getObject(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		return mapper.readTree(in);
	}

	public JsonNode getObject(String jsonStr) throws IOException {
		if (jsonStr == null) {
			return null;
		}
		return mapper.readTree(jsonStr);
	}

	public ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}
}
