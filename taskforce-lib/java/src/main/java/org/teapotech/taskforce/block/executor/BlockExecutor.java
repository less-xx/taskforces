/**
 * 
 */
package org.teapotech.taskforce.block.executor;

import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.BlockExecutorNotFoundException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;

/**
 * @author jiangl
 *
 */
public interface BlockExecutor {

	Object execute(BlockExecutionContext context)
			throws BlockExecutionException, InvalidBlockExecutorException, BlockExecutorNotFoundException;

}
