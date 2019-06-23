package org.teapotech.block.exception;

public class InvalidBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5233859602493107356L;

	public InvalidBlockException() {
	}

	public InvalidBlockException(String message) {
		super(message);
	}

	public InvalidBlockException(Throwable cause) {
		super(cause);
	}

	public InvalidBlockException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBlockException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
