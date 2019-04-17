/**
 * 
 */
package org.teapotech.block.executor.logic;

import java.util.List;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockMutation;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

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
		Boolean ifCondition = (Boolean) BlockExecutorUtils.execute(ifCondBlock, context);
		if (ifCondition) {
			Block statBlock = this.block.getStatements().get(idx).getBlock();
			return BlockExecutorUtils.execute(statBlock, context);
		}

		if (mut.getElseif() != null) {
			idx += 1;
			while (idx <= mut.getElseif()) {
				ifCondBlock = values.get(idx).getBlock();
				ifCondition = (Boolean) BlockExecutorUtils.execute(ifCondBlock, context);
				if (ifCondition) {
					Block statBlock = this.block.getStatements().get(idx).getBlock();
					return BlockExecutorUtils.execute(statBlock, context);
				}
				idx += 1;
			}
		}

		if (mut.getElse() != null) {
			Block statBlock = this.block.getStatements().get(idx).getBlock();
			return BlockExecutorUtils.execute(statBlock, context);
		}
		return null;
	}

}
