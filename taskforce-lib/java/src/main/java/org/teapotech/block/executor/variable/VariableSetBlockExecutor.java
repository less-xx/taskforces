package org.teapotech.block.executor.variable;

import java.util.Map;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Variable;

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

		Map<String, Variable> variables = context.getVariables();
		String fieldId = this.block.getField().getId();
		Variable var = variables.get(fieldId);

		if (this.block.getValues().isEmpty()) {
			throw new BlockExecutionException("Missing value for block, type: " + this.block.getType());
		}

		BlockValue bv = this.block.getValues().get(0);
		BlockExecutor be = context.getBlockExecutorFactory().createBlockExecutor(bv);
		Object value = be.execute(context);
		var.setValue((String) value);
		return variables.put(fieldId, var);
	}

}
