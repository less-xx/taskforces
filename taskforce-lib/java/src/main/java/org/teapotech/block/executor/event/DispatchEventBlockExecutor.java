/**
 * 
 */
package org.teapotech.block.executor.event;

import org.slf4j.Logger;
import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class DispatchEventBlockExecutor extends AbstractBlockExecutor {

	public DispatchEventBlockExecutor(Block block) {
		super(block);
	}

	public DispatchEventBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		Logger LOG = context.getLogger();
		BlockValue bv = block.getBlockValueByName("event", null);
		if (bv == null) {
			throw new BlockExecutionException("Missing event block");
		}
		NamedBlockEvent evt = (NamedBlockEvent) BlockExecutorUtils.execute(bv, context);
		if (evt == null) {
			throw new BlockExecutionException("Invalid event block.");
		}
		context.getEventDispatcher().dispatchBlockEvent(evt);
		LOG.info("Dispatched block event. Name: {}", evt.getEventName());

		return null;
	}

}
