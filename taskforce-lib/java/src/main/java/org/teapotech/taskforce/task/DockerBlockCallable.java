/**
 * 
 */
package org.teapotech.taskforce.task;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.model.Block;

/**
 * @author jiangl
 *
 */
public interface DockerBlockCallable {

	/**
	 * 
	 * @param block
	 * @return
	 * @throws BlockExecutionException
	 * @throws BlockExecutorNotFoundException
	 * @throws InvalidBlockException
	 */
	Object call(Block block) throws InvalidBlockExecutorException, BlockExecutionException,
			BlockExecutorNotFoundException, InvalidBlockException;
}
