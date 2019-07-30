/**
 * 
 */
package org.teapotech.block.executor.math;

import org.apache.commons.lang3.RandomUtils;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class MathRandomIntBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public MathRandomIntBlockExecutor(Block block) {
		super(block);
	}

	/**
	 * @param blockValue
	 */
	public MathRandomIntBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		if (block.getValues() == null | block.getValues().size() < 2) {
			throw new InvalidBlockException(this.block.getId(), this.block.getType(), "Missing FROM or TO values");
		}
		BlockValue fromBv = block.getValues().get(0);
		BlockValue toBv = block.getValues().get(1);

		Integer fromValue = (Integer) BlockExecutorUtils.execute(fromBv, context);
		Integer toValue = (Integer) BlockExecutorUtils.execute(toBv, context);
		Integer result = RandomUtils.nextInt(fromValue, toValue);
		return result;
	}

}
