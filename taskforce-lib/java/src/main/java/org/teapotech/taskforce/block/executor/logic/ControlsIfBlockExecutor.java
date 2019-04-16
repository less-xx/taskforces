/**
 * 
 */
package org.teapotech.taskforce.block.executor.logic;

import java.util.List;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockMutation;
import org.teapotech.taskforce.block.model.BlockValue;

/**
 * @author jiangl
 *
 */
public class ControlsIfBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public ControlsIfBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		BlockMutation mut = this.block.getMutation();
		List<BlockValue> values = this.block.getValues();
		int idx = 0;
		Block ifCondBlock = values.get(idx).getBlock();
		Boolean ifCondition = (Boolean) context.getBlockExecutorFactory().createBlockExecutor(ifCondBlock)
				.execute(context);
		if (ifCondition) {
			Block statBlock = this.block.getStatements().get(idx).getBlock();
			return context.getBlockExecutorFactory().createBlockExecutor(statBlock)
					.execute(context);
		} else if (mut.getElseif() != null) {
			idx += 1;
			while (idx <= mut.getElseif()) {
				ifCondBlock = values.get(idx).getBlock();
				ifCondition = (Boolean) context.getBlockExecutorFactory().createBlockExecutor(ifCondBlock)
						.execute(context);
				if (ifCondition) {
					Block statBlock = this.block.getStatements().get(idx).getBlock();
					return context.getBlockExecutorFactory().createBlockExecutor(statBlock)
							.execute(context);
				}
				idx += 1;
			}
		} else if (mut.getElse() != null) {
			idx += 1;
			Block statBlock = this.block.getStatements().get(idx).getBlock();
			return context.getBlockExecutorFactory().createBlockExecutor(statBlock)
					.execute(context);
		}
		return null;
	}

}
