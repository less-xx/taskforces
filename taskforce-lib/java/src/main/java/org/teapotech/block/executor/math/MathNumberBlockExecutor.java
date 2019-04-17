/**
 * 
 */
package org.teapotech.block.executor.math;

import org.apache.commons.lang3.math.NumberUtils;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;

/**
 * @author jiangl
 *
 */
public class MathNumberBlockExecutor extends AbstractBlockExecutor {

	public MathNumberBlockExecutor(Block block) {
		super(block);
	}

	public MathNumberBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		if (this.block != null) {
			Field field = this.block.getFieldByName("NUM", this.block.getFields().get(0));
			String strValue = field.getValue();
			return strToNumber(strValue);
		} else {
			Field field = this.shadow.getField();
			return strToNumber(field.getValue());
		}
	}

	private Number strToNumber(String input) throws BlockExecutionException {
		if (!NumberUtils.isParsable(input)) {
			throw new BlockExecutionException("Invalid field value: " + input);
		}
		if (input.indexOf(".") > 0) {
			return new Double(input);
		} else {
			return new Integer(input);
		}
	}

}
