/**
 * 
 */
package org.teapotech.taskforce.block.executor.loop;

import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.InvalidBlockException;
import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;
import org.teapotech.taskforce.block.model.Statement;
import org.teapotech.taskforce.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class ControlsRepeatExtBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public ControlsRepeatExtBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		BlockValue timesBv = this.block.getValues().get(0);

		Object times = context.getBlockExecutorFactory().createBlockExecutor(timesBv).execute(context);
		if (!(times instanceof Integer)) {
			throw new BlockExecutionException(
					"The value should be integer. Block type: " + this.block.getType() + ", id: " + this.block.getId());
		}
		int timesInt = ((Integer) times).intValue();
		if (this.block.getStatements() == null || this.block.getStatements().isEmpty()) {
			throw new InvalidBlockException(
					"Missing statements. Block type: " + this.block.getType() + ", id: " + this.block.getId());
		}
		Statement stmt = this.block.getStatements().get(0);
		Object result = null;
		for (int i = 0; i < timesInt; i++) {
			result = BlockExecutorUtils.execute(stmt.getBlock(), context);
		}
		return result;
	}

}
