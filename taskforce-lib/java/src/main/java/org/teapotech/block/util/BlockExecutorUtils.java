/**
 * 
 */
package org.teapotech.block.util;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Block.Next;
import org.teapotech.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class BlockExecutorUtils {

	public static Object execute(Block block, BlockExecutionContext context) throws InvalidBlockException,
			BlockExecutionException, InvalidBlockExecutorException, BlockExecutorNotFoundException {

		String name = Thread.currentThread().getName();
		BlockExecutionProgress beg = context.getBlockExecutionProgress().get(name);
		if (beg == null) {
			context.getLogger().error("Cannot find block execution thread by name: {}", name);
		}
		BlockExecutor executor = context.getBlockExecutorFactory().createBlockExecutor(context.getWorkspaceId(), block);
		beg.setBlock(block);
		beg.setBlockStatus(BlockStatus.Created);
		context.getLogger().info("Block id: [{}], type: [{}] is [{}]", block.getId(), block.getType(),
				beg.getBlockStatus());

		Object result = executor.execute(context);

		Next next = block.getNext();
		while (next != null) {
			result = context.getBlockExecutorFactory().createBlockExecutor(context.getWorkspaceId(), next.getBlock())
					.execute(context);
			if ("BREAK".equals(result)) {
				break;
			} else if ("CONTINUE".equals(result)) {
				continue;
			}
			next = next.getBlock().getNext();
		}
		return result;
	}

	public static Object execute(BlockValue bValue, BlockExecutionContext context) throws InvalidBlockException,
			BlockExecutionException, InvalidBlockExecutorException, BlockExecutorNotFoundException {
		BlockExecutor executor = context.getBlockExecutorFactory().createBlockExecutor(context.getWorkspaceId(),
				bValue);
		Block block = bValue.getBlock();
		if (block != null) {
			String name = Thread.currentThread().getName();
			BlockExecutionProgress beg = context.getBlockExecutionProgress().get(name);
			if (beg == null) {
				context.getLogger().error("Cannot find block execution thread by name: {}", name);
			}
			beg.setBlock(block);
			beg.setBlockStatus(BlockStatus.Created);
			context.getLogger().info("Block id: [{}], type: [{}] is [{}]", block.getId(), block.getType(),
					beg.getBlockStatus());
		}

		return executor.execute(context);
	}
}
