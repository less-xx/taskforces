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
public class FileAtPathBlockExecutor extends AbstractBlockExecutor {

	public FileAtPathBlockExecutor(Block block) {
		super(block);
	}

	public FileAtPathBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		BlockValue bv = this.block.getBlockValueByName("path", null);
		if (bv == null) {
			throw new InvalidBlockException(block.getId(), block.getType(), "Missing path value");
		}
		String path = (String) BlockExecutorUtils.execute(bv, context);
		File file = new File(path);
		if (!file.exists()) {
			throw new BlockExecutionException("Cannot find file at path " + file.getAbsolutePath());
		}

		return new File[] { file };
	}

}
