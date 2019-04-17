/**
 * 
 */
package org.teapotech.block.executor;

import java.util.HashMap;
import java.util.Map;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.model.Variable;

/**
 * @author jiangl
 *
 */
public class DefaultBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory = BlockExecutorFactory.build();
	private final Map<String, Variable> variables = new HashMap<>();

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public Map<String, Variable> getVariables() {
		return variables;
	}

}
