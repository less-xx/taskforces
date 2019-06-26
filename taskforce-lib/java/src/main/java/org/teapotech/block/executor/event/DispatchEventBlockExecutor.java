/**
 * 
 */
package org.teapotech.block.executor.event;

import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
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

		BlockValue bv = block.getBlockValueByName("messageName", null);
		String messageName = (String) BlockExecutorUtils.execute(bv, context);
		if (messageName == null) {
			throw new BlockExecutionException("Missing message name.");
		}
		NamedBlockEvent evt = new NamedBlockEvent(context.getWorkspaceId(), this.block.getType(), this.block.getId());
		context.getEventDispatcher().dispatchBlockEvent(evt);
		LOG.info("Dispatched block event. Name: {}", messageName);

		return null;
	}

}
