package org.teapotech.block.def;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CustomBlock implements AbstractBlock {

	protected static ObjectMapper mapper = new ObjectMapper();

	protected JsonNode loadBlockDefinitionTemplate() throws IOException {

		String blockType = getBlockType();

		return mapper.readTree(getClass().getClassLoader().getResourceAsStream(blockType + ".tpl.json"));
	}
}
