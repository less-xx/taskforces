/**
 * 
 */
package org.teapotech.block.executor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.teapotech.block.BlockExecutorFactory;

/**
 * @author jiangl
 *
 */
public class DefaultBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final Map<String, Object> variables = new HashMap<>();

	public DefaultBlockExecutionContext(BlockExecutorFactory blockExecutorFactory) {
		this.blockExecutorFactory = blockExecutorFactory;
	}

	public DefaultBlockExecutionContext() {
		this.blockExecutorFactory = BlockExecutorFactory.build();
	}

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public Object getVariable(String id) {
		return variables.get(id);
	}

	@Override
	public void setVariable(String id, Object value) {
		variables.put(id, value);
	}

	@Override
	public Collection<String> getAllVariableNames() {
		return variables.keySet();
	}
}
