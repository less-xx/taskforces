/**
 * 
 */
package org.teapotech.block.executor.text;

import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;

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
			field = this.block.getFieldByName("TEXT", this.block.getFields().get(0));
		} else {
			field = this.shadow.getField();
		}
		return field.getValue();
	}

}
