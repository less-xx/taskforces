/**
 * 
 */
package org.teapotech.block.executor.event;

import org.slf4j.Logger;
import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class EventWithParamBlockExecutor extends AbstractBlockExecutor {

	public EventWithParamBlockExecutor(Block block) {
		super(block);
	}

	public EventWithParamBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Logger LOG = context.getLogger();
		Field evtNameFld = block.getFieldByName("event_name", null);
		if (evtNameFld == null) {
			throw new BlockExecutionException("Missing event_name field value.");
		}
		NamedBlockEvent evt = new NamedBlockEvent(context.getWorkspaceId(), this.block.getType(), this.block.getId());
		evt.setEventName(evtNameFld.getValue());
		BlockValue bv = block.getBlockValueByName("parameter", null);
		if (bv != null) {
			String value = (String) BlockExecutorUtils.execute(bv, context);
			evt.setParameter(value);
		}
		LOG.info("Created block event. Name: {}", evt.getEventName());

		return evt;
	}

}
