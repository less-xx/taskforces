/**
 * 
 */
package org.teapotech.taskforce.block.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.block.BlockExecutorFactory;
import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.BlockExecutorNotFoundException;
import org.teapotech.taskforce.block.exception.InvalidBlockException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Variable;
import org.teapotech.taskforce.block.model.Workspace;

/**
 * @author jiangl
 *
 */
public class WorkspaceExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(WorkspaceExecutor.class);

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
			throws InvalidBlockException, BlockExecutorNotFoundException, InvalidBlockExecutorException,
			BlockExecutionException {

		List<Variable> variables = workspace.getVariables();
		variables.stream().forEach(v -> {
			context.getVariables().put(v.getId(), v);
			LOG.debug("Added variable {}", v);
		});

		Block startBlock = workspace.getBlock();
		BlockExecutorUtils.execute(startBlock, context);
	}
}
