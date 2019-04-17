/**
 * 
 */
package org.teapotech.taskforce.executor;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class DockerTaskExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public DockerTaskExecutor(Block block) {
		super(block);
	}

	/**
	 * @param blockValue
	 */
	public DockerTaskExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		String blockType = this.block.getType();
		return null;
	}

	protected Object runTask()

}
