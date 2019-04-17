/**
 * 
 */
package org.teapotech.block.executor;

import java.util.Map;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.model.Variable;

/**
 * @author jiangl
 *
 */
public interface BlockExecutionContext {

	BlockExecutorFactory getBlockExecutorFactory();

	Map<String, Variable> getVariables();

}
