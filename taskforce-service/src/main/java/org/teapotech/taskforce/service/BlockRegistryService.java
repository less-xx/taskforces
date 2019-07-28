/**
 * 
 */
package org.teapotech.taskforce.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistry;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Category;
import org.teapotech.block.model.Workspace;
import org.teapotech.taskforce.dto.BlockDefinitionDTO;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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

	private Map<String, Category> defaultCategories = new HashMap<>();

	@PostConstruct
	private void init() throws Exception {
		loadDefaultCategories();
	}

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

	public Workspace getToolboxConfiguration() {
		Workspace toolbox = new Workspace();
		blockRegistryManager.getBlockRegistries().stream().forEach(br -> {
			String category = br.getCategory();
			Category cat = toolbox.getCategoryByName(category, true);
			// if (br.getColour() != null) {
			// cat.setColour(String.valueOf(br.getColour()));
			// }
			try {
				Block tb = blockRegistryManager.getToolboxConfig(br.getType());
				if (tb == null) {
					tb = new Block();
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

	private void loadDefaultCategories() throws IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream("category-definition.json");
		ArrayNode an = (ArrayNode) JSONUtils.getObject(in);
		Iterator<JsonNode> it = an.iterator();
		while (it.hasNext()) {
			JsonNode n = it.next();
			Category c = new Category();
			String id = n.get("id").asText();
			c.setName(n.get("style").asText());
			c.setCategorystyle(n.get("style").asText());
			defaultCategories.put(id, c);
		}
	}

	public Category getCategoryByName(String name, boolean createIfNotExist) {
		String[] cc = name.split("\\s*/\\s*");

		Category cat = null;
		if (this.categories == null) {
			if (createIfNotExist) {
				this.categories = new ArrayList<>();
			} else {
				return cat;
			}
		}
		List<Category> cl = this.categories;
		String style = "";
		for (String cname : cc) {
			Optional<Category> op = cl.stream().filter(c -> c.getName().equalsIgnoreCase(cname)).findFirst();
			if (op.isPresent()) {
				cat = op.get();
				cl = cat.getCategories();
			} else {
				cat = new Category(cname);
				style = style + "_" + cname.toLowerCase();
				cat.setCategorystyle(style.substring(1));
				cl.add(cat);
				cl = cat.getCategories();
			}
			if (cl == null && createIfNotExist) {
				cl = new ArrayList<>();
				cat.setCategories(cl);
			}
		}
		return cat;
	}
	

}
