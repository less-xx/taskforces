package org.teapotech.block.exception;

public class InvalidBlockExecutorException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1045210986622134560L;

	public InvalidBlockExecutorException() {
	}

	public InvalidBlockExecutorException(String message) {
		super(message);
	}

	public InvalidBlockExecutorException(Throwable cause) {
		super(cause);
	}

	public InvalidBlockExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBlockExecutorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
