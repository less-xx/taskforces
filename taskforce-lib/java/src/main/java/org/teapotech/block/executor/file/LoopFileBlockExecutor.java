/**
 * 
 */
package org.teapotech.block.executor.file;

import java.io.File;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
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
		BlockValue filePathBLockValue = this.block.getBlockValueByName("filePath", null);
		File[] files = (File[]) BlockExecutorUtils.execute(filePathBLockValue, context);
		if (files == null) {
			return null;
		}

		String paramName = "file";
		Field paramNameField = this.block.getFieldByName("file", this.block.getFields().get(0));
		if (paramNameField != null) {
			paramName = paramNameField.getValue();
		}

		for (File f : files) {
			context.setVariable(paramName, f);
			Statement stmt = this.block.getStatementByName("action", this.block.getStatements().get(0));
			if (stmt != null) {
				Block b = stmt.getBlock();
				BlockExecutorUtils.execute(b, context);
			}

		}
		return null;
	}

}
