/**
 * 
 */
package org.teapotech.taskforce.block.executor.text;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;
import org.teapotech.taskforce.block.model.Field;

/**
 * @author jiangl
 *
 */
public class TextBlockExecutor extends AbstractBlockExecutor {

	public TextBlockExecutor(Block block) {
		super(block);
	}

	public TextBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Field field = null;
		if (this.block != null) {
			field = this.block.getField();
		} else {
			field = this.shadow.getField();
		}
		return field.getValue();
	}

}
