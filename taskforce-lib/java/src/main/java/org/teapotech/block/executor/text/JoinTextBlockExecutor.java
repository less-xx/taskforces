/**
 * 
 */
package org.teapotech.block.executor.text;

import java.util.List;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

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
			BlockExecutor be = fac.createBlockExecutor(bv);
			Object s = be.execute(context);
			result.append(s);
		}
		return result.toString();

	}

}
