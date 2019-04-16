/**
 * 
 */
package org.teapotech.taskforce.block.executor.text;

import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.Field;

/**
 * @author jiangl
 *
 */
public class TextBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public TextBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Field field = this.block.getField();
		return field.getValue();
	}

}
