/**
 * 
 */
package org.teapotech.block.executor.event;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
		Logger LOG = context.getLogger();
		BlockValue bv = block.getBlockValueByName("eventName", null);
		if (bv == null) {
			throw new BlockExecutionException("Missing event name block value.");
		}
		String eventName = (String) BlockExecutorUtils.execute(bv, context);
		if (StringUtils.isBlank(eventName)) {
			throw new BlockExecutionException("Missing event name.");
		}
		NamedBlockEvent evt = new NamedBlockEvent(context.getWorkspaceId(), this.block.getType(), this.block.getId());
		evt.setEventName(eventName);
		context.getEventDispatcher().dispatchBlockEvent(evt);
		LOG.info("Dispatched block event. Name: {}", eventName);

		return null;
	}

}
