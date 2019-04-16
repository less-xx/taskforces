/**
 * 
 */
package org.teapotech.taskforce.block.executor.text;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;

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
		Block valueBlock = this.block.getValues().get(0).getBlock();
		Object value = context.getBlockExecutorFactory().createBlockExecutor(valueBlock).execute(context);
		System.out.println(value);
		return null;
	}

}
