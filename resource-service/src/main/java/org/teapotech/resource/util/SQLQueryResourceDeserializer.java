/**
 * 
 */
package org.teapotech.resource.util;

import java.io.IOException;

import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.db.sql.SQLQueryResource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author lessdev
 *
 */
public class SQLQueryResourceDeserializer extends ParameterizedResourceDeserializer<SQLQueryResource> {

	private final static String PROP_BOUND_PARAMETERS = "boundParameters";
	private final static String PROP_USER_PARAMETERS = "userParameters";

	@SuppressWarnings("rawtypes")
	@Override
	public SQLQueryResource deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		SQLQueryResource res = new SQLQueryResource();
		JsonNode jn = p.getCodec().readTree(p);
		JsonNode bpn = jn.get(PROP_BOUND_PARAMETERS);
		if (bpn.isArray()) {
			ArrayNode an = (ArrayNode) bpn;
			for (JsonNode n : an) {
				ResourceParameter rp = buildResourceParameter(n);
				res.updateBoundParameterValue(rp.getName(), rp.getValue());
			}
		}
		bpn = jn.get(PROP_USER_PARAMETERS);
		if (bpn.isArray()) {
			ArrayNode an = (ArrayNode) bpn;
			for (JsonNode n : an) {
				res.updateUserParameter(buildResourceParameter(n));
			}
		}
		return res;
	}

}
