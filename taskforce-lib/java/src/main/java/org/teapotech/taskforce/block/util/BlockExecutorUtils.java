/**
 * 
 */
package org.teapotech.taskforce.block.util;

import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.BlockExecutorNotFoundException;
import org.teapotech.taskforce.block.exception.InvalidBlockException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Block.Next;
import org.teapotech.taskforce.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class BlockExecutorUtils {

	public static Object execute(Block block, BlockExecutionContext context)
			throws InvalidBlockException, BlockExecutionException, InvalidBlockExecutorException,
			BlockExecutorNotFoundException {
		Object result = context.getBlockExecutorFactory().createBlockExecutor(block).execute(context);
		Next next = block.getNext();
		while (next != null) {
			result = context.getBlockExecutorFactory().createBlockExecutor(next.getBlock()).execute(context);
			next = next.getBlock().getNext();
		}
		return result;
	}

	public static Object execute(BlockValue bValue, BlockExecutionContext context)
			throws InvalidBlockException, BlockExecutionException, InvalidBlockExecutorException,
			BlockExecutorNotFoundException {
		return context.getBlockExecutorFactory().createBlockExecutor(bValue).execute(context);

	}
}
