/**
 * 
 */
package org.teapotech.block.executor.math;

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
public class MathRoundBlockExecutor extends AbstractBlockExecutor {

	public MathRoundBlockExecutor(Block block) {
		super(block);
	}

	public MathRoundBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		Field field = this.block.getFieldByName("OP", this.block.getFields().get(0));
		String opValue = field.getValue();

		BlockValue numValueBv = block.getBlockValueByName("NUM", block.getValues().get(0));
		Number numValue = (Number) BlockExecutorUtils.execute(numValueBv, context);

		if (opValue.equalsIgnoreCase("rounddown")) {
			return Math.floor(numValue.doubleValue());
		} else if (opValue.equalsIgnoreCase("roundup")) {
			return Math.ceil(numValue.doubleValue());
		} else {
			return Math.round(numValue.doubleValue());
		}

	}

}
