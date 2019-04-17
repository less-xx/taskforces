/**
 * 
 */
package org.teapotech.taskforce.block.executor.math;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;
import org.teapotech.taskforce.block.model.Field;

/**
 * @author jiangl
 *
 */
public class MathNumberBlockExecutor extends AbstractBlockExecutor {

	public MathNumberBlockExecutor(Block block) {
		super(block);
	}

	public MathNumberBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		if (this.block != null) {
			Field field = this.block.getField();
			return new Integer(field.getValue());
		} else {
			Field field = this.shadow.getField();
			return new Integer(field.getValue());
		}
	}

}
