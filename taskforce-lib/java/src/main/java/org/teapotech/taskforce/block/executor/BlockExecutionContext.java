/**
 * 
 */
package org.teapotech.taskforce.block.executor;

import java.util.Map;

import org.teapotech.taskforce.block.BlockExecutorFactory;
import org.teapotech.taskforce.block.model.Variable;

/**
 * @author jiangl
 *
 */
public interface BlockExecutionContext {

	BlockExecutorFactory getBlockExecutorFactory();

	Map<String, Variable> getVariables();

}
