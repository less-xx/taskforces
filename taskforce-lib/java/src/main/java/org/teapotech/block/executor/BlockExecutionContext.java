/**
 * 
 */
package org.teapotech.block.executor;

import org.teapotech.block.BlockExecutorFactory;

/**
 * @author jiangl
 *
 */
public interface BlockExecutionContext {

	BlockExecutorFactory getBlockExecutorFactory();

	Object getVariable(String id);

	void setVariable(String id, Object value);

}
