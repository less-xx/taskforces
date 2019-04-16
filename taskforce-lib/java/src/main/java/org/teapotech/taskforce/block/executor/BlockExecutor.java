/**
 * 
 */
package org.teapotech.taskforce.block.executor;

/**
 * @author jiangl
 *
 */
public interface BlockExecutor {

	Object execute(BlockExecutionContext context) throws Exception;

}
