/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistry;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.model.toolbox.Category;
import org.teapotech.block.model.toolbox.Toolbox;
import org.teapotech.block.model.toolbox.ToolboxBlock;
import org.teapotech.taskforce.dto.BlockDefinitionDTO;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author jiangl
 *
 */
@Service
public class BlockRegistryService {

	private static final Logger LOG = LoggerFactory.getLogger(BlockRegistryService.class);

	@Autowired
	BlockRegistryManager blockRegistryManager;

	@Autowired
	BlockExecutorFactory blockFactory;

	public Collection<BlockRegistry> getAllBlockRegistries() {
		return blockRegistryManager.getBlockRegistries();
	}

	public List<BlockDefinitionDTO> getCustomBlockDefinitions() {
		List<BlockDefinitionDTO> result = new ArrayList<>();
		blockRegistryManager.getBlockRegistries().stream().forEach(br -> {
			if (!StringUtils.isBlank(br.getDefinition())) {
				try {
					JsonNode defNode = JSONUtils.getObject(br.getDefinition());
					result.add(new BlockDefinitionDTO(br.getType(), defNode));
				} catch (Exception e) {
					LOG.error("Invalid block definition. Ignore it. \n{}", e.getMessage());
				}
			}
		});
		return result;
	}

	public Toolbox getToolboxConfiguration() {
		Toolbox toolbox = new Toolbox();
		blockRegistryManager.getBlockRegistries().stream().forEach(br -> {
			String category = br.getCategory();
			Category cat = toolbox.getCategoryByName(category);

			try {
				ToolboxBlock tb = blockRegistryManager.getToolboxConfig(br.getType());
				if (tb == null) {
					tb = new ToolboxBlock();
					tb.setType(br.getType());
				}
				if (cat.getBlocks() == null) {
					cat.setBlocks(new ArrayList<>());
				}
				cat.getBlocks().add(tb);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		});
		return toolbox;
	}

}
