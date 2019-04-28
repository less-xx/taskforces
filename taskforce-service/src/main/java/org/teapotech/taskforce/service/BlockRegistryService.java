/**
 * 
 */
package org.teapotech.taskforce.service;

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
import org.teapotech.block.BlockExecutorFactory.BlockRegistry;
import org.teapotech.taskforce.dto.BlockRegistryDTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author jiangl
 *
 */
@Service
public class BlockRegistryService {

	private static final Logger LOG = LoggerFactory.getLogger(BlockRegistryService.class);
	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	BlockExecutorFactory blockFactory;

	public List<BlockRegistry> getAllBlockRegistries() {
		return blockFactory.getBlockRegistries();
	}

	public Map<String, Set<BlockRegistryDTO>> getCategorizedBlockRegistries() {

		final Map<String, Set<BlockRegistryDTO>> result = new TreeMap<>();
		blockFactory.getBlockRegistries().stream().forEach(br -> {
			Set<BlockRegistryDTO> brs = result.get(br.getCategory());
			if (brs == null) {
				brs = new TreeSet<>(new Comparator<BlockRegistryDTO>() {

					@Override
					public int compare(BlockRegistryDTO o1, BlockRegistryDTO o2) {
						return o1.getType().compareTo(o2.getType());
					}

				});
				result.put(br.getCategory(), brs);
			}
			if (StringUtils.isBlank(br.getDefinition())) {
				brs.add(new BlockRegistryDTO(br));
			} else {
				try {
					JsonNode defNode = mapper.readTree(br.getDefinition());
					brs.add(new BlockRegistryDTO(br, defNode));
				} catch (Exception e) {
					LOG.error("Invalid block definition. Ignore it. \n{}", e.getMessage());
				}
			}

		});
		return result;
	}
}
