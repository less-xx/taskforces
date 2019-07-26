/**
 * 
 */
package org.teapotech.block.executor.start_stop;

import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public class ExitBlockExecutor extends AbstractBlockExecutor {

	public ExitBlockExecutor(Block block) {
		super(block);
	}

	public ExitBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		context.getLogger().info("Stopping workspace {}.", context.getWorkspaceId());
		context.getEventDispatcher()
				.dispatchWorkspaceExecutionEvent(
						new WorkspaceExecutionEvent(context.getWorkspaceId(), Status.Stopping));

		return null;
	}

}
