/**
 * 
 */
package org.teapotech.taskforce.block.executor.math;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Field;

/**
 * @author jiangl
 *
 */
public class MathNumberBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public MathNumberBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Field field = this.block.getField();
		return new Integer(field.getValue());
	}

}
