/**
 * 
 */
package org.teapotech.block.def.file;

import java.util.Collection;

import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.FilePathBlockExecutor;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jiangl
 *
 */
public class FilePathBlock extends CustomBlockDefinition implements CustomResourcePathLoaderSupport {

	private CustomResourcePathLoader customResourcePathLoader;

	@Override
	public String getBlockDefinition() {
		try {
			ObjectNode tplNode = (ObjectNode) loadBlockDefinitionTemplateObject();
			Collection<FileSystemPath> csConf = customResourcePathLoader.getAllFileSystemPaths();
			ArrayNode optionsNode = (ArrayNode) tplNode.get("args0").get(0).get("options");
			for (FileSystemPath csc : csConf) {
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
		return "file_path";
	}

	@Override
	public void setCustomResourcePathLoader(CustomResourcePathLoader loader) {
		assert loader != null;
		this.customResourcePathLoader = loader;

	}

	@Override
	public String getCategory() {
		return "File Operation";
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return FilePathBlockExecutor.class;
	}

}
