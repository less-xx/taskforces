/**
 * 
 */
package org.teapotech.block.executor.math;

import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class MathArithmeticBlockExecutor extends AbstractBlockExecutor {

	public MathArithmeticBlockExecutor(Block block) {
		super(block);
	}

	public MathArithmeticBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		Field field = this.block.getFieldByName("OP", this.block.getFields().get(0));
		String opValue = field.getValue();

		BlockValue aValueBv = block.getBlockValueByName("A", block.getValues().get(0));
		Number aValue = (Number) BlockExecutorUtils.execute(aValueBv, context);

		BlockValue bValueBv = block.getBlockValueByName("B", block.getValues().get(0));
		Number bValue = (Number) BlockExecutorUtils.execute(bValueBv, context);

		if (opValue.equalsIgnoreCase("add")) {
			return aValue.doubleValue() + bValue.doubleValue();
		} else if (opValue.equalsIgnoreCase("minus")) {
			return aValue.doubleValue() - bValue.doubleValue();
		} else if (opValue.equalsIgnoreCase("MULTIPLY")) {
			return aValue.doubleValue() * bValue.doubleValue();
		} else if (opValue.equalsIgnoreCase("DIVIDE")) {
			return aValue.doubleValue() / bValue.doubleValue();
		}
		throw new InvalidBlockException(this.block.getId(), this.block.getType(), "Unknown operator: " + opValue);
	}

}
