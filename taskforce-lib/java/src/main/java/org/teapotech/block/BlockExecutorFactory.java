/**
 * 
 */
package org.teapotech.block;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private final Map<String, Class<? extends BlockExecutor>> registry = new HashMap<>();
	private final DockerClient dockerClient;

	public static BlockExecutorFactory build() {
		return build(null);
	}

	public static BlockExecutorFactory build(DockerClient client) {
		ObjectMapper mapper = new ObjectMapper();
		BlockExecutorFactory fac = new BlockExecutorFactory(client);
		try (InputStream in = BlockExecutorFactory.class.getClassLoader()
				.getResourceAsStream("block-executor-registry.json");) {
			List<BlockRegistry> regs = mapper.readValue(in, new TypeReference<List<BlockRegistry>>() {
			});
			for (BlockRegistry br : regs) {
				@SuppressWarnings("unchecked")
				Class<BlockExecutor> c = (Class<BlockExecutor>) Class.forName(br.getExecutorClass());
				fac.registry.put(br.type, c);
				LOG.info("Registered block executor {} = {}", br.type, c);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return fac;
	}

	private BlockExecutorFactory() {
		this(null);
	}

	private BlockExecutorFactory(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public void registerExecutor(String blockType, Class<? extends BlockExecutor> executorClass) {
		this.registry.put(blockType, executorClass);
		LOG.info("Registered block executor {} = {}", blockType, executorClass);
	}

	public BlockExecutor createBlockExecutor(Block block)
			throws InvalidBlockException, BlockExecutorNotFoundException, InvalidBlockExecutorException {
		Class<? extends BlockExecutor> c = registry.get(block.getType());
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
		Class<? extends BlockExecutor> c = registry.get(blockType);
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

	public static class BlockRegistry {

		String type;
		String category;
		String executorClass;

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

	}
}