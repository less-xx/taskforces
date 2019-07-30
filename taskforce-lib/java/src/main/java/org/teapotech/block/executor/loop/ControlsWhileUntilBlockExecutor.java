/**
 * 
 */
package org.teapotech.block.executor.loop;

import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Statement;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class ControlsWhileUntilBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public ControlsWhileUntilBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		String mode = this.block.getFieldByName("MODE", this.block.getFields().get(0)).getValue();

		BlockValue expressionBv = this.block.getBlockValueByName("BOOL", this.block.getValues().get(0));
		if (expressionBv == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(), "Missing expression block");
		}
		Boolean expressionValue = (Boolean) BlockExecutorUtils.execute(expressionBv, context);

		boolean whileTrue = mode.equalsIgnoreCase("while");

		Statement stmt = this.block.getStatementByName("DO", this.block.getStatements().get(0));
		if (stmt == null) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(), "Missing statement block");
		}
		while (whileTrue && expressionValue) {
			Object result = BlockExecutorUtils.execute(stmt.getBlock(), context);
			if (result != null) {
				if (result.equals("BREAK")) {
					break;
				} else if (result.equals("CONTINUE")) {
					continue;
				}
			}
			expressionValue = (Boolean) BlockExecutorUtils.execute(expressionBv, context);
		}

		return null;
	}

}
