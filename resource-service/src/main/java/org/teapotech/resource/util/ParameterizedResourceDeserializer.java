/**
 * 
 */
package org.teapotech.resource.util;

import java.io.IOException;

import org.teapotech.resource.ResourceParameter;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author lessdev
 *
 */
@SuppressWarnings("rawtypes")
public abstract class ParameterizedResourceDeserializer<T> extends JsonDeserializer<T> {

	public final static String PROP_BOUND_PARAMETERS = "boundParameters";
	public final static String PROP_USER_PARAMETERS = "userParameters";

	@SuppressWarnings("unchecked")
	protected ResourceParameter buildResourceParameter(JsonNode node) throws IOException {
		JsonNode n = node.get("name");
		if (n == null || n.isMissingNode() || n.isNull()) {
			throw new IOException("Missing field 'name'.");
		}
		String pname = n.asText();
		boolean required = true;
		n = node.get("required");
		if (n == null || n.isMissingNode() || n.isNull()) {
			required = true;
		} else {
			required = n.asBoolean();
		}
		n = node.get("type");
		if (n == null || n.isMissingNode() || n.isNull()) {
			throw new IOException("Missing field 'type'.");
		}
		Class<?> type = null;
		try {
			type = Class.forName(n.asText());
		} catch (ClassNotFoundException e) {
			throw new IOException("Cannot find type [" + n.asText() + "]");
		}
		Object value = convertValue(type, node.get("value"));
		return new ResourceParameter(pname, type, required, value);
	}

	private Object convertValue(Class<?> type, JsonNode valueNode) throws IOException {
		if (valueNode == null || valueNode.isMissingNode() || valueNode.isNull()) {
			return null;
		}
		if (type.equals(String.class)) {
			return valueNode.asText();
		}
		if (type.equals(Integer.class)) {
			return valueNode.asInt();
		}
		if (type.equals(Long.class)) {
			return valueNode.asLong();
		}
		if (type.equals(Float.class) || type.equals(Double.class)) {
			return valueNode.asDouble();
		}
		if (type.equals(Boolean.class)) {
			return valueNode.asBoolean();
		}
		throw new IOException("Unsupported type [" + type + "]");
	}
}
