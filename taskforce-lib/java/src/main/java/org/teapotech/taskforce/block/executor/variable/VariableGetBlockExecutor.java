package org.teapotech.taskforce.block.executor.variable;

import java.util.Map;

import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Variable;

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

		Map<String, Variable> variables = context.getVariables();
		String fieldId = this.block.getField().getId();
		Variable var = variables.get(fieldId);

		if (this.block.getValues().isEmpty()) {
			throw new BlockExecutionException("Missing value for block, type: " + this.block.getType());
		}
		return var.getValue();
	}

}
