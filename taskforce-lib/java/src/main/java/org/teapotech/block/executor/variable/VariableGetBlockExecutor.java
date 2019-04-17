package org.teapotech.block.executor.variable;

import java.util.Map;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Variable;

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
		return var.getValue();
	}

}
