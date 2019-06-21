/**
 * 
 */
package org.teapotech.block.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Variable;
import org.teapotech.block.model.Workspace;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public class WorkspaceExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(WorkspaceExecutor.class);

	private final BlockExecutionContext context;
	private final Workspace workspace;
	private final ThreadGroup threadGroup;
	private BlockExecutionThread[] blockExecutionThreads;
	private BlockExecutionMonitoringThread monitoringThread;

	public WorkspaceExecutor(Workspace workspace, BlockExecutionContext context) {
		this.context = context;
		this.workspace = workspace;
		this.threadGroup = new ThreadGroup("group-" + context.getWorkspaceId());
		this.threadGroup.setDaemon(true);
	}

	public BlockExecutionContext getBlockExecutionContext() {
		return context;
	}

	public BlockExecutorFactory getBlockExecutorFactory() {
		return context.getBlockExecutorFactory();
	}

	public void execute() {

		context.getWorkspaceEventDispatcher()
				.dispatchWorkspaceExecutionEvent(new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Running));

		List<Variable> variables = workspace.getVariables();
		if (variables != null) {
			variables.stream().forEach(v -> {
				context.setVariable(v.getValue(), "");
				LOG.debug("Added variable: {}", v.getValue());
			});
		}

		List<Block> startBlocks = workspace.getBlocks();
		blockExecutionThreads = new BlockExecutionThread[startBlocks.size()];

		for (int i = 0; i < blockExecutionThreads.length; i++) {
			BlockExecutionThread bt = new BlockExecutionThread(startBlocks.get(i), this.threadGroup);
			blockExecutionThreads[i] = bt;
			bt.start();
		}

		monitoringThread = new BlockExecutionMonitoringThread("monitoring-" + context.getWorkspaceId());
		monitoringThread.start();
	}

	public void stop() {
		context.getWorkspaceEventDispatcher()
				.dispatchWorkspaceExecutionEvent(
						new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Stopping));
		this.context.setStopped(true);
		this.threadGroup.interrupt();
	}

	private class BlockExecutionThread extends Thread {

		private final Block startBlock;

		public BlockExecutionThread(Block startBlock, ThreadGroup threadGroup) {
			super(threadGroup, "block." + startBlock.getId());
			this.startBlock = startBlock;
			LOG.info("Created block execution thread. Block ID: {}, Type: {}, Group: {}", startBlock.getId(),
					startBlock.getType(), threadGroup.getName());
		}

		@Override
		public void run() {
			try {
				BlockExecutorUtils.execute(startBlock, context);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			LOG.info("Block execution thread exited. Group: {}, Active: {}", threadGroup.getName(),
					threadGroup.activeCount());
		}
	}

	private class BlockExecutionMonitoringThread extends Thread {

		public BlockExecutionMonitoringThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			while (true) {
				boolean running = false;
				for (BlockExecutionThread t : blockExecutionThreads) {
					running = running || t.isAlive();
				}
				if (!running) {
					break;
				}
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					LOG.warn("Block monitoring thread is interrupted.");
					break;
				}
			}
			LOG.info("All block execution threads are stopped.");
			context.getWorkspaceEventDispatcher()
					.dispatchWorkspaceExecutionEvent(
							new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Stopped));
			if (!context.isStopped()) {
				context.setStopped(true);
			}
			threadGroup.destroy();
			context.destroy();
		}
	}
}
