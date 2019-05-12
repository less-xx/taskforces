/**
 * 
 */
package org.teapotech.block.executor.file;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

/**
 * @author lessdev
 *
 */
public class CopyFileBlockExecutor extends AbstractBlockExecutor {

	public CopyFileBlockExecutor(Block block) {
		super(block);
	}

	public CopyFileBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		return null;
	}

}
