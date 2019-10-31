/**
 * 
 */
package org.teapotech.resource.db;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author jiangl
 *
 */
public class PORT_Parameter extends DatabaseConnectionParameter {

	public static final String TYPE = "PORT";

	@Override
	public void validate(Object value) throws DatabaseConnectionException {
		super.validate(value);

		int val = -1;
		if (value == null) {
			value = defaultValue;
		}
		if (value instanceof Integer) {
			val = (Integer) value;
		} else if (value instanceof String) {
			try {
				val = Integer.parseInt((String) value);
			} catch (Exception e) {
				throw new DatabaseConnectionException("Invalid port value. It should be an integer.");
			}
		}
		if (val < 1 || val > 65535) {
			throw new DatabaseConnectionException("Invalid port number");
		}
	}

	public Integer convertValue(Object value) {
		if (value instanceof String) {
			return NumberUtils.createInteger((String) value);
		}
		return (Integer) value;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
