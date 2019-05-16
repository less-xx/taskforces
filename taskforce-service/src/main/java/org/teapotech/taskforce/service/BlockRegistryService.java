/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistry;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.model.Block;
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

	public Map<String, Set<Block>> getToolboxConfiguration() {
		final Map<String, Set<Block>> result = new TreeMap<>();
		blockRegistryManager.getBlockRegistries().stream().forEach(br -> {
			String category = br.getCategory();
			if (StringUtils.isBlank(category)) {
				LOG.error("Missing category for block, type: {}.", br.getType());
				category = "Unknown";
			}
			Set<Block> blocks = result.get(category);
			if (blocks == null) {
				blocks = new TreeSet<>(new Comparator<Block>() {

					@Override
					public int compare(Block o1, Block o2) {
						return o1.getType().compareTo(o2.getType());
					}

				});
				result.put(category, blocks);
			}
			try {
				Block b = blockRegistryManager.getToolboxConfig(br.getType());
				blocks.add(b);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		});
		return result;
	}

}
