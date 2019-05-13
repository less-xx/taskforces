/**
 * 
 */
package org.teapotech.block.def.file;

import java.util.List;

import org.teapotech.block.annotation.CustomBlock;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.entity.CustomResourcePath;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jiangl
 *
 */
@CustomBlock
public class ResourcePathBlock extends CustomBlockDefinition implements CustomResourcePathLoaderSupport {

	private CustomResourcePathLoader customResourcePathLoader;

	@Override
	public String getBlockDefinition() {
		try {
			ObjectNode tplNode = (ObjectNode) loadBlockDefinitionTemplateObject();
			List<CustomResourcePath> csConf = customResourcePathLoader.getAllCustomResourcePaths();
			ArrayNode optionsNode = (ArrayNode) tplNode.get("args0").get(0).get("options");
			for (CustomResourcePath csc : csConf) {
				ArrayNode opNode = JSONUtils.createArrayNode();
				opNode.add(csc.getName()).add(csc.getId());
				optionsNode.add(opNode);
			}
			return JSONUtils.getJSON(tplNode);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getBlockType() {
		return "resource_path";
	}

	@Override
	public void setCustomResourcePathLoader(CustomResourcePathLoader loader) {
		assert loader != null;
		this.customResourcePathLoader = loader;

	}

	@Override
	public String getCategory() {
		return "Resource";
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return null;
	}

}
