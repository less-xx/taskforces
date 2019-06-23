/**
 * 
 */
package org.teapotech.taskforce.util;

/**
 * @author jiangl
 *
 */
public class InvalidVariableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6882050343472362008L;

	public InvalidVariableException() {
		super();
	}

	public InvalidVariableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidVariableException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidVariableException(String message) {
		super(message);
	}

	public InvalidVariableException(Throwable cause) {
		super(cause);
	}

}
