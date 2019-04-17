/**
 * 
 */
package org.teapotech.block.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Variable;
import org.teapotech.block.model.Workspace;

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
		if (variables != null) {
			variables.stream().forEach(v -> {
				context.getVariables().put(v.getId(), v);
				LOG.debug("Added variable {}", v);
			});
		}

		Block startBlock = workspace.getBlock();
		BlockExecutorUtils.execute(startBlock, context);
	}
}
