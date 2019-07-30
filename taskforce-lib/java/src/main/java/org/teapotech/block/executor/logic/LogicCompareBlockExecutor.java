/**
 * 
 */
package org.teapotech.block.executor.logic;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.util.BlockExecutorUtils;

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

		updateBlockStatus(context, BlockStatus.Running);

		Field opField = this.block.getFieldByName("OP", this.block.getFields().get(0));
		if (opField == null) {
			throw new InvalidBlockExecutorException("Missing operator field");
		}
		String operator = opField.getValue();
		List<BlockValue> values = this.block.getValues();
		if (values.size() < 2) {
			throw new InvalidBlockExecutorException("The comparison need to values");
		}
		BlockValue valueB0 = values.get(0);
		BlockValue valueB1 = values.get(1);
		Object value0 = BlockExecutorUtils.execute(valueB0, context);
		Object value1 = BlockExecutorUtils.execute(valueB1, context);
		return compare(value0, value1, operator);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Boolean compare(Object v1, Object v2, String operator) throws Exception {

		if (operator.equalsIgnoreCase("eq")) {
			if (v1 instanceof String) {
				return Objects.equals(v1, v2.toString());
			} else if (v2 instanceof String) {
				return Objects.equals(v1.toString(), v2);
			} else {
				return Objects.equals(v1, v2);
			}

		} else if (operator.equalsIgnoreCase("ne")) {
			return !Objects.equals(v1, v2);
		} else {
			if (ClassUtils.isAssignable(v1.getClass(), Number.class)
					&& ClassUtils.isAssignable(v2.getClass(), Number.class)) {
				Number n1 = (Number) v1;
				Number n2 = (Number) v2;
				if (operator.equalsIgnoreCase("gt")) {
					return n1.doubleValue() > n2.doubleValue();
				} else if (operator.equalsIgnoreCase("gte")) {
					return n1.doubleValue() >= n2.doubleValue();
				} else if (operator.equalsIgnoreCase("lt")) {
					return n1.doubleValue() < n2.doubleValue();
				} else if (operator.equalsIgnoreCase("lte")) {
					return n1.doubleValue() <= n2.doubleValue();
				}
				throw new InvalidBlockExecutorException("Unknown comparision operator " + operator);

			} else if (!v1.getClass().equals(v2.getClass())) {
				throw new BlockExecutionException(
						"Types of the inputs are not identical. " + v1.getClass() + " vs " + v2.getClass());
			}
			if (ClassUtils.isAssignable(v1.getClass(), Comparable.class)) {

				Comparable c1 = (Comparable) v1;
				Comparable c2 = (Comparable) v2;
				if (operator.equalsIgnoreCase("gt")) {
					return c1.compareTo(c2) > 0;
				} else if (operator.equalsIgnoreCase("gte")) {
					return c1.compareTo(c2) >= 0;
				} else if (operator.equalsIgnoreCase("lt")) {
					return c1.compareTo(c2) < 0;
				} else if (operator.equalsIgnoreCase("lte")) {
					return c1.compareTo(c2) <= 0;
				}
				throw new InvalidBlockExecutorException("Unknown comparision operator " + operator);
			}
			throw new BlockExecutionException(
					"Type of the inputs are not comparable. " + v1.getClass());
		}
	}

}
