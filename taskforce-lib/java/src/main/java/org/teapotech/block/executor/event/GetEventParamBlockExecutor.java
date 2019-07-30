/**
 * 
 */
package org.teapotech.block.executor.event;

import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;

/**
 * @author jiangl
 *
 */
public class GetEventParamBlockExecutor extends AbstractBlockExecutor {

	public GetEventParamBlockExecutor(Block block) {
		super(block);
	}

	public GetEventParamBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		Field evtNameFld = block.getFieldByName("event_name", null);
		if (evtNameFld == null) {
			throw new BlockExecutionException("Missing event_name field value.");
		}
		NamedBlockEvent evt = (NamedBlockEvent) context.getLocalVariable(evtNameFld.getValue());
		if (evt == null) {
			throw new BlockExecutionException("Cannot find event named " + evtNameFld.getValue());
		}
		return evt.getParameter();
	}

}
