/**
 * 
 */
package org.teapotech.block.executor;

import java.util.Collection;

import org.teapotech.block.BlockExecutorFactory;

/**
 * @author jiangl
 *
 */
public interface BlockExecutionContext {

	String getWorkspaceId();

	BlockExecutorFactory getBlockExecutorFactory();

	Object getVariable(String id);

	void setVariable(String id, Object value);

	Collection<String> getAllVariableNames();

}
