/**
 * 
 */
package org.teapotech.block.util;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Block.Next;
import org.teapotech.block.model.BlockValue;

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
			if ("BREAK".equals(result)) {
				break;
			} else if ("CONTINUE".equals(result)) {
				continue;
			}
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
