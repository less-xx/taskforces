/**
 * 
 */
package org.teapotech.block;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.docker.DockerBlockExecutor;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.docker.client.DockerClient;

/**
 * @author jiangl
 *
 */
public class BlockExecutorFactory {

	private static Logger LOG = LoggerFactory.getLogger(BlockExecutorFactory.class);

	private final Map<String, Class<? extends BlockExecutor>> blockExecutors = new HashMap<>();
	private final DockerClient dockerClient;
	private final List<BlockRegistry> blockRegistries = new ArrayList<>();

	public static BlockExecutorFactory build() {
		return build(null);
	}

	@SuppressWarnings("unchecked")
	public static BlockExecutorFactory build(DockerClient client) {
		ObjectMapper mapper = new ObjectMapper();
		List<BlockRegistry> regs = null;
		BlockExecutorFactory fac = new BlockExecutorFactory(client);
		try (InputStream in = BlockExecutorFactory.class.getClassLoader()
				.getResourceAsStream("block-executor-registry.json");) {
			regs = mapper.readValue(in, new TypeReference<List<BlockRegistry>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		if (regs == null) {
			return fac;
		}

		for (BlockRegistry br : regs) {

			String defFileName = br.getType() + "_def.json";
			try (InputStream in = BlockExecutorFactory.class.getClassLoader()
					.getResourceAsStream(defFileName);) {
				if (in != null) {
					String def = IOUtils.toString(in, "UTF-8");
					br.setDefinition(def);
				}

				Class<BlockExecutor> c = (Class<BlockExecutor>) Class.forName(br.getExecutorClass());
				fac.blockExecutors.put(br.type, c);
				LOG.info("Registered block executor {} = {}", br.type, c);

			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}

		}
		if (!regs.isEmpty()) {
			fac.blockRegistries.addAll(regs);
		}
		return fac;
	}

	private BlockExecutorFactory() {
		this(null);
	}

	private BlockExecutorFactory(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public void registerExecutor(String blockType, String category, String definition,
			Class<? extends BlockExecutor> executorClass) {
		BlockRegistry registry = new BlockRegistry();
		registry.setType(blockType);
		registry.setCategory(category);
		registry.setDefinition(definition);
		registry.setExecutorClass(executorClass.getName());
		this.blockRegistries.add(registry);
		this.blockExecutors.put(blockType, executorClass);
		LOG.info("Registered block executor {} = {}", blockType, executorClass);
	}

	public BlockExecutor createBlockExecutor(Block block)
			throws InvalidBlockException, BlockExecutorNotFoundException, InvalidBlockExecutorException {
		Class<? extends BlockExecutor> c = blockExecutors.get(block.getType());
		if (c == null) {
			throw new BlockExecutorNotFoundException("Block executor not register for type " + block.getType());
		}
		try {
			if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
				Constructor<? extends BlockExecutor> cons = c.getConstructor(Block.class, DockerClient.class);
				return cons.newInstance(block, this.dockerClient);
			} else {
				Constructor<? extends BlockExecutor> cons = c.getConstructor(Block.class);
				return cons.newInstance(block);
			}

		} catch (Exception e) {
			throw new InvalidBlockExecutorException(e.getMessage(), e);
		}
	}

	public BlockExecutor createBlockExecutor(BlockValue blockValue)
			throws InvalidBlockException, BlockExecutorNotFoundException, InvalidBlockExecutorException {
		String blockType = null;
		if (blockValue.getBlock() != null) {
			blockType = blockValue.getBlock().getType();
		} else if (blockValue.getShadow() != null) {
			blockType = blockValue.getShadow().getType();
		} else {
			throw new InvalidBlockException(
					"Block value should have either block or shadow, name: " + blockValue.getName());
		}
		Class<? extends BlockExecutor> c = blockExecutors.get(blockType);
		if (c == null) {
			throw new BlockExecutorNotFoundException("Block executor not register for type " + blockType);
		}
		try {
			Constructor<? extends BlockExecutor> cons = null;
			if (blockValue.getBlock() != null) {
				if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
					cons = c.getConstructor(Block.class, DockerClient.class);
					return cons.newInstance(blockValue.getBlock(), this.dockerClient);
				} else {
					cons = c.getConstructor(Block.class);
					return cons.newInstance(blockValue.getBlock());
				}
			} else {
				if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
					cons = c.getConstructor(BlockValue.class, DockerClient.class);
					return cons.newInstance(blockValue, this.dockerClient);
				} else {
					cons = c.getConstructor(BlockValue.class);
					return cons.newInstance(blockValue);
				}

			}
		} catch (Exception e) {
			throw new InvalidBlockExecutorException(e.getMessage(), e);
		}
	}

	public List<BlockRegistry> getBlockRegistries() {
		return blockRegistries;
	}

	public static class BlockRegistry {

		String type;
		String category;
		String executorClass;
		String definition;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getExecutorClass() {
			return executorClass;
		}

		public void setExecutorClass(String executorClass) {
			this.executorClass = executorClass;
		}

		public void setDefinition(String definition) {
			this.definition = definition;
		}

		public String getDefinition() {
			return definition;
		}
	}
}
