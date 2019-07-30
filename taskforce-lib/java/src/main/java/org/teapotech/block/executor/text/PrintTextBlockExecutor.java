/**
 * 
 */
package org.teapotech.block.executor.text;

import org.teapotech.block.exception.InvalidBlockException;
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
public class PrintTextBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public PrintTextBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		BlockValue valueBlock = this.block.getValues().get(0);
		if (valueBlock == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(),
					"Missing value. Block type: " + this.block.getType() + ", id: " + this.block.getId());
		}
		Object value = BlockExecutorUtils.execute(valueBlock, context);
		System.out.println(value);
		return null;
	}

}
