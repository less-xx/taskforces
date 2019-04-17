/**
 * 
 */
package org.teapotech.taskforce.block.executor.text;

import org.teapotech.taskforce.block.exception.InvalidBlockException;
import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;

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
		BlockValue valueBlock = this.block.getValues().get(0);
		if (valueBlock == null) {
			throw new InvalidBlockException(
					"Missing value. Block type: " + this.block.getType() + ", id: " + this.block.getId());
		}
		Object value = context.getBlockExecutorFactory().createBlockExecutor(valueBlock).execute(context);
		System.out.println(value);
		return null;
	}

}
