package org.teapotech.base.exception;

public class UserNotLogonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -940519923481437039L;

	public UserNotLogonException() {
	}

	public UserNotLogonException(String message) {
		super(message);
	}

	public UserNotLogonException(Throwable cause) {
		super(cause);
	}

	public UserNotLogonException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotLogonException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
