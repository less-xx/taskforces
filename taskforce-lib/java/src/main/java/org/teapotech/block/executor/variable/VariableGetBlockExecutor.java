package org.teapotech.block.executor.variable;

import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;

/**
 * 
 * @author jiangl
 *
 */
public class VariableGetBlockExecutor extends AbstractBlockExecutor {

	public VariableGetBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		String var = this.block.getFieldByName("VAR", this.block.getFields().get(0)).getValue();
		Object value = context.getVariable(var);
		if (value == null) {
			throw new InvalidBlockException("Cannot find variable: " + var);
		}
		return value;
	}

}
