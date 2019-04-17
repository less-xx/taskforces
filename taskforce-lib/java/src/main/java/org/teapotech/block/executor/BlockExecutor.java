/**
 * 
 */
package org.teapotech.block.executor;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockExecutorException;

/**
 * @author jiangl
 *
 */
public interface BlockExecutor {

	Object execute(BlockExecutionContext context)
			throws BlockExecutionException, InvalidBlockExecutorException, BlockExecutorNotFoundException;

}
