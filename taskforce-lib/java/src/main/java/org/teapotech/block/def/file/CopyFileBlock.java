package org.teapotech.block.def.file;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.CopyFileBlockExecutor;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.entity.CustomResourcePath;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CopyFileBlock extends CustomBlockDefinition implements CustomResourcePathLoaderSupport {

	private final static Logger LOG = LoggerFactory.getLogger(CopyFileBlock.class);

	private CustomResourcePathLoader customResourcePathLoader;

	@Override
	public String getBlockDefinition() {
		try {
			ObjectNode tplNode = (ObjectNode) loadBlockDefinitionTemplateObject();
			List<FileSystemPath> csConf = customResourcePathLoader.getAllFileSystemPaths();
			ArrayNode optionsNode = (ArrayNode) tplNode.get("args0").get(1).get("options");
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
		return "copy_files";
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
		return CopyFileBlockExecutor.class;
	}

}
