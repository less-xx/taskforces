/**
 * 
 */
package org.teapotech.taskforce.block.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.model.Block;

/**
 * @author jiangl
 *
 */
public abstract class AbstractBlockExecutor implements BlockExecutor {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	protected final Block block;

	public AbstractBlockExecutor(Block block) {
		this.block = block;
	}

	@Override
	public final Object execute(BlockExecutionContext context) throws BlockExecutionException {
		try {
			Object result = doExecute(context);
			LOG.debug("Block executed, type: {}, id: {}", block.getType(), block.getId());
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
