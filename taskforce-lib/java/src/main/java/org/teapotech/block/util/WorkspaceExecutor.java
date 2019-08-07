/**
 * 
 */
package org.teapotech.block.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.def.event.HandleEventBlock;
import org.teapotech.block.def.start_stop.StartBlock;
import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
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
	private BlockExecutionMonitoringThread monitoringThread;

	public WorkspaceExecutor(Workspace workspace, BlockExecutionContext context) {
		this.context = context;
		this.workspace = workspace;
		this.threadGroup = new ThreadGroup("group-" + context.getWorkspaceId());
		this.threadGroup.setDaemon(true);
		LOG.info("Created workspace executor. ID: {}", context.getWorkspaceId());
	}

	public BlockExecutionContext getBlockExecutionContext() {
		return context;
	}

	public BlockExecutorFactory getBlockExecutorFactory() {
		return context.getBlockExecutorFactory();
	}

	public void execute() {

		context.getEventDispatcher()
				.dispatchWorkspaceExecutionEvent(new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Running));

		List<Variable> variables = workspace.getVariables();
		if (variables != null) {
			variables.stream().forEach(v -> {
				context.setVariable(v.getValue(), "");
				LOG.debug("Added variable: {}", v.getValue());
			});
		}

		List<Block> startBlocks = workspace.getBlocks();

		BlockExecutionThread entryPointThread = null;

		List<String> eventBlockThreadNames = new ArrayList<String>();
		for (Block block : startBlocks) {

			if (block.getType().equals(StartBlock.TYPE)) {
				entryPointThread = new BlockExecutionThread(block, this.threadGroup);
				context.getBlockExecutionProgress().put(entryPointThread.getName(),
						new BlockExecutionProgress(entryPointThread.getName()));

			} else if (block.getType().equals(HandleEventBlock.TYPE)) {
				BlockExecutionThread bt = new BlockExecutionThread(block, this.threadGroup);
				context.getBlockExecutionProgress().put(bt.getName(), new BlockExecutionProgress(bt.getName()));
				bt.start();
				eventBlockThreadNames.add(bt.getName());
			}
		}

		if (entryPointThread != null) {
			boolean initialized = false;
			while (!initialized) {
				boolean b = true;
				for (String tn : eventBlockThreadNames) {
					BlockExecutionProgress beg = context.getBlockExecutionProgress().get(tn);
					if (beg != null) {
						b = b && (beg.getBlockStatus() == BlockStatus.Running);
					}
				}
				initialized = b;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					break;
				}
			}

			entryPointThread.start();
			startMonitoring();
		}
	}

	public void startMonitoring() {
		monitoringThread = new BlockExecutionMonitoringThread("monitoring-" + context.getWorkspaceId());
		monitoringThread.start();
	}

	public void stop() {
		this.context.setStopped(true);
		this.threadGroup.interrupt();
	}

	private class BlockExecutionThread extends Thread {

		private final Block startBlock;

		public BlockExecutionThread(Block startBlock, ThreadGroup threadGroup) {
			super(threadGroup, "bet." + startBlock.getId());
			this.startBlock = startBlock;
			LOG.info("Created block execution thread. Block ID: {}, Type: {}, Group: {}", startBlock.getId(),
					startBlock.getType(), threadGroup.getName());
		}

		@Override
		public void run() {
			context.getLogger().info("Thread {} start running.", getName());
			try {
				BlockExecutorUtils.execute(startBlock, context);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}

			String name = Thread.currentThread().getName();
			BlockExecutionProgress beg = context.getBlockExecutionProgress().get(name);
			if (beg == null) {
				context.getLogger().error("Cannot find block execution thread by name: {}", name);
			}
			beg.setBlockStatus(BlockStatus.Stopped);
			context.getLogger().info("Thread {} stopped.", getName());
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
				Thread[] blockExecutionThreads = new Thread[threadGroup.activeCount()];
				threadGroup.enumerate(blockExecutionThreads);
				if (blockExecutionThreads.length < 1) {
					LOG.warn("The workspace executor is not running.");
					break;
				}
				for (Thread t : blockExecutionThreads) {
					if (t.getName().equals(this.getName())) {
						continue;
					}
					running = running || t.isAlive();
				}
				if (!running) {
					break;
				}
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					LOG.warn("Block monitoring thread is interrupted.");
					break;
				}
			}
			LOG.info("All block execution threads are stopped.");
			context.getEventDispatcher().dispatchWorkspaceExecutionEvent(
					new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Stopped));
			if (!context.isStopped()) {
				context.setStopped(true);
			}
			context.destroy();
		}
	}
}
