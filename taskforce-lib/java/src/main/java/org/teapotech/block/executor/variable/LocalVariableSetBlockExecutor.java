package org.teapotech.block.executor.variable;

import org.apache.commons.lang3.StringUtils;
import org.teapotech.block.exception.InvalidBlockException;
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
public class LocalVariableSetBlockExecutor extends AbstractBlockExecutor {

	public LocalVariableSetBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		String var = this.block.getFieldByName("var", this.block.getFields().get(0)).getValue();
		if (StringUtils.isBlank(var)) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(),
					"Missing field 'var'.");
		}
		BlockValue bValue = this.block.getBlockValueByName("value", null);
		if (bValue == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(),
					"Missing 'value' block.");
		}
		Object value = BlockExecutorUtils.execute(bValue, context);
		if (value == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(),
					"Empty 'value' block.");
		}
		context.setLocalVariable(var, value);
		context.getLogger().info("Set local variable: {}", var);
		return null;
	}

}
