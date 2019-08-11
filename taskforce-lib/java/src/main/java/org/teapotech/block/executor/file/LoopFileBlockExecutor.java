/**
 * 
 */
package org.teapotech.block.executor.file;

import java.io.File;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.model.Statement;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author lessdev
 *
 */
public class LoopFileBlockExecutor extends AbstractBlockExecutor {

	public LoopFileBlockExecutor(Block block) {
		super(block);
	}

	public LoopFileBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		BlockValue filePathBLockValue = this.block.getBlockValueByName("path", null);
		File[] files = (File[]) BlockExecutorUtils.execute(filePathBLockValue, context);
		if (files == null) {
			return null;
		}

		String var = "file";
		Field varField = this.block.getFieldByName("var", this.block.getFields().get(0));
		if (varField != null) {
			var = varField.getValue();
		}

		for (File f : files) {
			context.setLocalVariable(var, f);
			Statement stmt = this.block.getStatementByName("action", this.block.getStatements().get(0));
			if (stmt != null) {
				Block b = stmt.getBlock();
				BlockExecutorUtils.execute(b, context);
			}

		}
		return null;
	}

}
