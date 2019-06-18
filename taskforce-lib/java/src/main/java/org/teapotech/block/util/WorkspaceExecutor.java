/**
 * 
 */
package org.teapotech.block.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
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
	private final Workspace workspace;
	private final ThreadGroup threadGroup;

	public WorkspaceExecutor(Workspace workspace, BlockExecutionContext context) {
		this.context = context;
		this.workspace = workspace;
		this.threadGroup = new ThreadGroup("group-" + workspace.getId());
		this.threadGroup.setDaemon(true);
	}

	public BlockExecutionContext getBlockExecutionContext() {
		return context;
	}

	public BlockExecutorFactory getBlockExecutorFactory() {
		return context.getBlockExecutorFactory();
	}

	public void execute() {

		List<Variable> variables = workspace.getVariables();
		if (variables != null) {
			variables.stream().forEach(v -> {
				context.setVariable(v.getValue(), "");
				LOG.debug("Added variable: {}", v.getValue());
			});
		}

		List<Block> startBlocks = workspace.getBlocks();

		for (final Block startBlock : startBlocks) {

			new Thread(this.threadGroup, new Runnable() {

				@Override
				public void run() {
					try {
						LOG.info("Created thread for block, ID: {}, Type: {}, Group: {}", startBlock.getId(),
								startBlock.getType(), threadGroup.getName());
						BlockExecutorUtils.execute(startBlock, context);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
					LOG.info("Block thread exited. Group: {}, Active: {}", threadGroup.getName(),
							threadGroup.activeCount());
				}
			}).start();
		}

	}

	public void destroy() {
		context.destroy();
		this.threadGroup.interrupt();
	}
}
