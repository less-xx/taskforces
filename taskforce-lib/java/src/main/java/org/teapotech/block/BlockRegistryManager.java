package org.teapotech.block;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.def.BlockDefinition;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.util.JSONUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class BlockRegistryManager {

	private static Logger LOG = LoggerFactory.getLogger(BlockRegistryManager.class);
	private final List<BlockRegistry> blockRegistries = new ArrayList<>();

	private CustomResourcePathLoader customResourcePathLoader;

	public CustomResourcePathLoader getCustomResourcePathLoader() {
		return customResourcePathLoader;
	}

	public void setCustomResourcePathLoader(CustomResourcePathLoader customResourcePathLoader) {
		this.customResourcePathLoader = customResourcePathLoader;
	}

	public List<BlockRegistry> getBlockRegistries() {
		return blockRegistries;
	}

	public void loadBlockRegistries() {
		loadDefaultBlockRegistry();
		loadCustomBlockDefinitions();
	}

	private void loadDefaultBlockRegistry() {
		List<BlockRegistry> regs = null;
		try (InputStream in = BlockExecutorFactory.class.getClassLoader()
				.getResourceAsStream("block-executor-registry.json");) {
			regs = JSONUtils.getObject(in, new TypeReference<List<BlockRegistry>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		if (regs == null) {
			return;
		}
		for (BlockRegistry br : regs) {
			this.blockRegistries.add(br);
			LOG.info("Registered block Type: {}, Executor class: {}", br.getType(), br.getExecutorClass());
		}

	}

	private void loadCustomBlockDefinitions() {

		Reflections reflections = new Reflections("org.teapotech");
		Set<Class<? extends BlockDefinition>> classes = reflections.getSubTypesOf(BlockDefinition.class);
		for (Class<? extends BlockDefinition> c : classes) {
			if (Modifier.isAbstract(c.getModifiers())) {
				continue;
			}
			LOG.info("Found block definition class: {}", c);
			try {
				BlockDefinition blockDef = c.newInstance();
				if (ClassUtils.isAssignable(c, CustomResourcePathLoaderSupport.class)) {
					((CustomResourcePathLoaderSupport) blockDef).setCustomResourcePathLoader(customResourcePathLoader);
					LOG.debug("Assign {} to {}", customResourcePathLoader, blockDef);
				}
				BlockRegistry reg = new BlockRegistry();
				reg.setDefinition(blockDef.getBlockDefinition());
				reg.setType(blockDef.getBlockType());
				reg.setCategory(blockDef.getCategory());
				reg.setExecutorClass(blockDef.getExecutorClass().getName());
				this.blockRegistries.add(reg);
				LOG.info("Registered custom block, Type: {}, Executor class: {}", reg.getType(),
						reg.getExecutorClass());
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public void registerBlock(BlockRegistry reg) {
		this.blockRegistries.add(reg);
		LOG.info("Registered custom block, Type: {}, Executor class: {}", reg.getType(), reg.getExecutorClass());
	}
}
