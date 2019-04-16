/**
 * 
 */
package org.teapotech.taskforce.block.executor.logic;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.teapotech.taskforce.block.exception.BlockExecutionException;
import org.teapotech.taskforce.block.exception.InvalidBlockExecutorException;
import org.teapotech.taskforce.block.executor.AbstractBlockExecutor;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.model.Block;
import org.teapotech.taskforce.block.model.BlockValue;
import org.teapotech.taskforce.block.model.Field;

/**
 * @author jiangl
 *
 */
public class LogicCompareBlockExecutor extends AbstractBlockExecutor {

	/**
	 * @param block
	 */
	public LogicCompareBlockExecutor(Block block) {
		super(block);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Field opField = this.block.getField();
		if (opField == null) {
			throw new InvalidBlockExecutorException("Missing operator field");
		}
		String operator = opField.getValue();
		List<BlockValue> values = this.block.getValues();
		if (values.size() < 2) {
			throw new InvalidBlockExecutorException("The comparison need to values");
		}
		Block valueB0 = values.get(0).getBlock();
		Block valueB1 = values.get(1).getBlock();
		Object value0 = context.getBlockExecutorFactory().createBlockExecutor(valueB0).execute(context);
		Object value1 = context.getBlockExecutorFactory().createBlockExecutor(valueB1).execute(context);
		return compare(value0, value1, operator);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Boolean compare(Object v1, Object v2, String operator) throws Exception {

		if (operator.equalsIgnoreCase("eq")) {
			return Objects.equals(v1, v2);
		} else if (!operator.equalsIgnoreCase("eq")) {
			return !Objects.equals(v1, v2);
		} else {
			if (!v1.getClass().equals(v2.getClass())) {
				throw new BlockExecutionException(
						"Types of the inputs are not identical. " + v1.getClass() + " vs " + v2.getClass());
			}
			if (ClassUtils.isAssignable(v1.getClass(), Comparable.class)) {

				Comparable c1 = (Comparable) v1;
				Comparable c2 = (Comparable) v2;
				if (operator.equalsIgnoreCase("gt")) {
					return c1.compareTo(c2) > 0;
				} else if (operator.equalsIgnoreCase("ge")) {
					return c1.compareTo(c2) >= 0;
				} else if (operator.equalsIgnoreCase("lt")) {
					return c1.compareTo(c2) < 0;
				} else if (operator.equalsIgnoreCase("le")) {
					return c1.compareTo(c2) <= 0;
				}
				throw new InvalidBlockExecutorException("Unknown comparision operator " + operator);
			}
			throw new BlockExecutionException(
					"Type of the inputs are not comparable. " + v1.getClass());
		}

	}

}
