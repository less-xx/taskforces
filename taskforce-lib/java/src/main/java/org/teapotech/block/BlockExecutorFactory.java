/**
 * 
 */
package org.teapotech.block;

import java.lang.reflect.Constructor;
import java.util.HashMap;
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
import org.teapotech.block.support.BlockEventListenerSupport;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.event.BlockEventListenerFactory;

import com.spotify.docker.client.DockerClient;

/**
 * @author jiangl
 *
 */
public class BlockExecutorFactory {

	private static Logger LOG = LoggerFactory.getLogger(BlockExecutorFactory.class);

	private final Map<String, Class<? extends BlockExecutor>> blockExecutors = new HashMap<>();
	private final DependencyRepository dependencyRepo;
	private final BlockRegistryManager blockRegistryManager;
	private final BlockEventListenerFactory blockEventListenerFactory;

	public static BlockExecutorFactory build(BlockRegistryManager blockRegistryManager) {
		return build(blockRegistryManager, null, null);
	}

	@SuppressWarnings("unchecked")
	public static BlockExecutorFactory build(BlockRegistryManager blockRegistryManager,
			DependencyRepository dependencyRepo,
			BlockEventListenerFactory blockEventListenerFactory) {
		BlockExecutorFactory fac = new BlockExecutorFactory(blockRegistryManager, dependencyRepo,
				blockEventListenerFactory);

		for (BlockRegistry br : fac.blockRegistryManager.getBlockRegistries()) {
			try {
				Class<BlockExecutor> c = (Class<BlockExecutor>) Class.forName(br.getExecutorClass());
				fac.blockExecutors.put(br.type, c);

				LOG.info("Registered block executor {} = {}", br.type, c);
			} catch (Exception e) {

			}
		}
		return fac;
	}

	private BlockExecutorFactory(BlockRegistryManager blockRegistryManager) {
		this(blockRegistryManager, null, null);
	}

	private BlockExecutorFactory(BlockRegistryManager blockRegistryManager, DependencyRepository dependencyRepo,
			BlockEventListenerFactory blockEventListenerFactory) {
		this.blockRegistryManager = blockRegistryManager;
		this.dependencyRepo = dependencyRepo;
		this.blockEventListenerFactory = blockEventListenerFactory;
	}

	public BlockExecutor createBlockExecutor(String workspaceId, Block block)
			throws InvalidBlockException, BlockExecutorNotFoundException, InvalidBlockExecutorException {
		Class<? extends BlockExecutor> c = blockExecutors.get(block.getType());
		if (c == null) {
			throw new BlockExecutorNotFoundException("Block executor not register for type " + block.getType());
		}
		try {

			BlockExecutor be = null;

			if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
				Constructor<? extends BlockExecutor> cons = c.getConstructor(Block.class, DockerClient.class);
				be = cons.newInstance(block, this.dependencyRepo.getDockerClient());
			} else {
				Constructor<? extends BlockExecutor> cons = c.getConstructor(Block.class);
				be = cons.newInstance(block);
			}

			if (ClassUtils.isAssignable(c, CustomResourcePathLoaderSupport.class)) {
				((CustomResourcePathLoaderSupport) be)
						.setCustomResourcePathLoader(this.blockRegistryManager.getCustomResourcePathLoader());
			}

			if (ClassUtils.isAssignable(c, BlockEventListenerSupport.class)) {
				((BlockEventListenerSupport) be)
						.setBlockEventListener(blockEventListenerFactory.createBlockEventListener(workspaceId, block));
			}

			return be;

		} catch (Exception e) {
			throw new InvalidBlockExecutorException(e.getMessage(), e);
		}
	}

	public BlockExecutor createBlockExecutor(String workspaceId, BlockValue blockValue)
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

			BlockExecutor be = null;

			Constructor<? extends BlockExecutor> cons = null;
			if (blockValue.getBlock() != null) {
				if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
					cons = c.getConstructor(Block.class, DockerClient.class);
					be = cons.newInstance(blockValue.getBlock(), this.dependencyRepo.getDockerClient());
				} else {
					cons = c.getConstructor(Block.class);
					be = cons.newInstance(blockValue.getBlock());
				}
			} else {
				if (ClassUtils.isAssignable(c, DockerBlockExecutor.class)) {
					cons = c.getConstructor(BlockValue.class, DockerClient.class);
					be = cons.newInstance(blockValue, this.dependencyRepo.getDockerClient());
				} else {
					cons = c.getConstructor(BlockValue.class);
					be = cons.newInstance(blockValue);
				}
			}

			if (ClassUtils.isAssignable(c, CustomResourcePathLoaderSupport.class)) {
				((CustomResourcePathLoaderSupport) be)
						.setCustomResourcePathLoader(this.blockRegistryManager.getCustomResourcePathLoader());
			}

			return be;

		} catch (Exception e) {
			throw new InvalidBlockExecutorException(e.getMessage(), e);
		}
	}

}
