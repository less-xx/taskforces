/**
 * 
 */
package org.teapotech.block.executor.text;

import java.util.List;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

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

		StringBuilder result = new StringBuilder();
		List<BlockValue> values = this.block.getValues();
		for (BlockValue bv : values) {
			Object s = BlockExecutorUtils.execute(bv, context);
			result.append(s);
		}
		return result.toString();

	}

}
