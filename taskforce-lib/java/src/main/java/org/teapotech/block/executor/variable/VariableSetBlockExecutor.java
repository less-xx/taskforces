package org.teapotech.block.executor.variable;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * 
 * @author jiangl
 *
 */
public class VariableSetBlockExecutor extends AbstractBlockExecutor {

	public VariableSetBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		String var = this.block.getFieldByName("VAR", this.block.getFields().get(0)).getValue();

		if (this.block.getValues().isEmpty()) {
			throw new BlockExecutionException("Missing value for block, type: " + this.block.getType());
		}

		BlockValue bv = this.block.getBlockValueByName("VAR", this.block.getValues().get(0));
		Object value = BlockExecutorUtils.execute(bv, context);
		context.setVariable(var, value);
		return value;
	}

}
