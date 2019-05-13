package org.teapotech.block.def;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class CustomBlockDefinition implements BlockDefinition {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	protected JsonNode loadBlockDefinitionTemplateObject() throws IOException {

		return JSONUtils.getObject(loadBlockDefinitionTemplate());
	}

	protected String loadBlockDefinitionTemplate() throws IOException {
		String blockType = getBlockType();
		InputStream in = getClass().getClassLoader().getResourceAsStream(blockType + ".tpl.json");
		if (in == null) {
			return null;
		}
		return IOUtils.toString(in, "UTF-8");
	}
}
