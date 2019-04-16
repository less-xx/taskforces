/**
 * 
 */
package org.teapotech.taskforce.block.executor.text;

import java.util.List;

import org.teapotech.taskforce.block.BlockExecutorFactory;
import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.executor.BlockExecutor;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class JoinTextBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public JoinTextBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		BlockExecutorFactory fac = context.getBlockExecutorFactory();
		StringBuilder result = new StringBuilder();
		List<BlockValue> values = this.block.getValues();
		for (BlockValue bv : values) {
			Block b = bv.getBlock();
			if (b != null) {
				BlockExecutor be = fac.createBlockExecutor(b);
				Object s = be.execute(context);
				result.append(s);
			}
		}
		return result.toString();

	}

}
