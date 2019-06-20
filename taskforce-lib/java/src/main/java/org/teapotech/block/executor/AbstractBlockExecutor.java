/**
 * 
 */
package org.teapotech.block.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Shadow;

/**
 * @author jiangl
 *
 */
public abstract class AbstractBlockExecutor implements BlockExecutor {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	protected final Block block;
	protected final Shadow shadow;

	public AbstractBlockExecutor(Block block) {
		this.block = block;
		this.shadow = null;
	}

	public AbstractBlockExecutor(BlockValue blockValue) {
		this.block = blockValue.getBlock();
		this.shadow = blockValue.getShadow();
	}

	@Override
	public final Object execute(BlockExecutionContext context) throws BlockExecutionException {
		if (context.isStopped()) {
			LOG.info("Execution is stopped.");
			return null;
		}
		try {
			if (this.block != null) {
				LOG.debug("Executing block, type: {}, id: {}", block.getType(), block.getId());
			} else {
				LOG.debug("Executing shadow, type: {}, id: {}", shadow.getType(), shadow.getId());
			}
			Object result = doExecute(context);
			if (this.block != null) {
				LOG.debug("Block executed, type: {}, id: {}", block.getType(), block.getId());
			} else {
				LOG.debug("Shadow executed, type: {}, id: {}", shadow.getType(), shadow.getId());
			}
			return result;
		} catch (Exception e) {
			if (e instanceof BlockExecutionException) {
				throw (BlockExecutionException) e;
			}
			throw new BlockExecutionException(e.getMessage(), e);
		}
	}

	abstract protected Object doExecute(BlockExecutionContext context) throws Exception;

}
