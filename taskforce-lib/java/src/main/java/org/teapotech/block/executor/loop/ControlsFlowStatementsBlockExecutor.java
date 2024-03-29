/**
 * 
 */
package org.teapotech.block.executor.loop;

import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Field;

/**
 * @author jiangl
 *
 */
public class ControlsFlowStatementsBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public ControlsFlowStatementsBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		Field flowField = block.getFieldByName("FLOW", block.getFields().get(0));
		if (flowField == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(), "Missing field FLOW");
		}
		return flowField.getValue();
	}

}
