/**
 * 
 */
package org.teapotech.taskforce.block;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.block.exception.BlockExecutorNotFoundException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;
import org.teapotech.taskforce.block.executor.BlockExecutor;
import org.teapotech.taskforce.block.model.Block;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author jiangl
 *
 */
public class BlockExecutorFactory {

	private static Logger LOG = LoggerFactory.getLogger(BlockExecutorFactory.class);

	private final Map<String, Class<BlockExecutor>> registry = new HashMap<>();

	public static BlockExecutorFactory build() {
		ObjectMapper mapper = new ObjectMapper();
		BlockExecutorFactory fac = new BlockExecutorFactory();
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

	public BlockExecutor createBlockExecutor(Block block)
			throws BlockExecutorNotFoundException, InstantiationException, InvalidBlockExecutorException {
		Class<BlockExecutor> c = registry.get(block.getType());
		if (c == null) {
			throw new BlockExecutorNotFoundException("Block executor not register for type " + block.getType());
		}
		try {
			Constructor<BlockExecutor> cons = c.getConstructor(Block.class);
			return cons.newInstance(block);
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
