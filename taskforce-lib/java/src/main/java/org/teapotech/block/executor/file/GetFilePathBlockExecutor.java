/**
 * 
 */
package org.teapotech.block.executor.file;

import java.io.File;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author lessdev
 *
 */
public class GetFilePathBlockExecutor extends AbstractBlockExecutor {

	public GetFilePathBlockExecutor(Block block) {
		super(block);
	}

	public GetFilePathBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		BlockValue bv = this.block.getBlockValueByName("file", null);
		if (bv == null) {
			throw new InvalidBlockException(block.getId(), block.getType(), "Missing file value");
		}
		File file = (File) BlockExecutorUtils.execute(bv, context);
		if (!file.exists()) {
			throw new BlockExecutionException("Cannot find file. ");
		}

		return file.getAbsolutePath();
	}

}
