/**
 * 
 */
package org.teapotech.resource.util;

import java.io.IOException;

import org.teapotech.resource.db.sql.SQLResource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author lessdev
 *
 */
@SuppressWarnings("rawtypes")
public class SQLResourceSerializer extends JsonSerializer<SQLResource> {

	@Override
	public void serialize(SQLResource value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeObjectField("boundParameters", value.getBoundParameters());
		gen.writeObjectField("userParameters", value.getUserParameters());
		gen.writeEndObject();
	}

}
