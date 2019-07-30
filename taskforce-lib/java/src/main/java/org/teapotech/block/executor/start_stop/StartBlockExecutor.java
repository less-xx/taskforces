/**
 * 
 */
package org.teapotech.block.executor.start_stop;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class StartBlockExecutor extends AbstractBlockExecutor {

	public StartBlockExecutor(Block block) {
		super(block);
	}

	public StartBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		context.getLogger().info("Workspace {} start running.", context.getWorkspaceId());
		updateBlockStatus(context, BlockStatus.Running);
		return null;
	}

}
