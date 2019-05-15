/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.Collection;
import java.util.Comparator;
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
import org.teapotech.taskforce.dto.BlockRegistryDTO;
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

	public Map<String, Set<BlockRegistryDTO>> getCategorizedBlockRegistries() {

		final Map<String, Set<BlockRegistryDTO>> result = new TreeMap<>();
		blockRegistryManager.getBlockRegistries().stream().forEach(br -> {
			String category = br.getCategory();
			if (StringUtils.isBlank(category)) {
				LOG.error("Missing category for block, type: {}.", br.getType());
				category = "Unknown";
			}
			Set<BlockRegistryDTO> brs = result.get(category);
			if (brs == null) {
				brs = new TreeSet<>(new Comparator<BlockRegistryDTO>() {

					@Override
					public int compare(BlockRegistryDTO o1, BlockRegistryDTO o2) {
						return o1.getType().compareTo(o2.getType());
					}

				});
				result.put(category, brs);
			}
			if (StringUtils.isBlank(br.getDefinition())) {
				brs.add(new BlockRegistryDTO(br));
			} else {
				try {
					JsonNode defNode = JSONUtils.getObject(br.getDefinition());
					brs.add(new BlockRegistryDTO(br, defNode));
				} catch (Exception e) {
					LOG.error("Invalid block definition. Ignore it. \n{}", e.getMessage());
				}
			}

		});
		return result;
	}
}
