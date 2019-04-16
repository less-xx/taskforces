/**
 * 
 */
package org.teapotech.taskforce.block.util;

import org.teapotech.taskforce.block.BlockExecutorFactory;
import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.BlockExecutorNotFoundException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Block.Next;
import org.teapotech.taskforce.block.model.Workspace;

/**
 * @author jiangl
 *
 */
public class WorkspaceExecutor {

	private final BlockExecutionContext context;
	private final BlockExecutorFactory factory;

	public WorkspaceExecutor(BlockExecutionContext context, BlockExecutorFactory factory) {
		this.context = context;
		this.factory = factory;
	}

	public BlockExecutionContext getBlockExecutionContext() {
		return context;
	}

	public BlockExecutorFactory getBlockExecutorFactory() {
		return factory;
	}

	public void execute(Workspace workspace)
			throws BlockExecutorNotFoundException, InvalidBlockExecutorException, BlockExecutionException {

		Block startBlock = workspace.getBlock();
		this.factory.createBlockExecutor(workspace.getBlock()).execute(context);
		Next next = startBlock.getNext();
		while (next != null) {
			this.factory.createBlockExecutor(next.getBlock()).execute(context);
			next = next.getBlock().getNext();
		}
	}
}
