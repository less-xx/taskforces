/**
 * 
 */
package org.teapotech.taskforce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author jiangl
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockDefinitionDTO {

	private String type;
	private JsonNode definition;

	public BlockDefinitionDTO() {
	}

	public BlockDefinitionDTO(String type, JsonNode definition) {
		this.type = type;
		this.definition = definition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JsonNode getDefinition() {
		return definition;
	}

	public void setDefinition(JsonNode definition) {
		this.definition = definition;
	}

}
