package org.teapotech.block.exception;

public class BlockExecutionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 737550822236029249L;

	public BlockExecutionException() {
	}

	public BlockExecutionException(String message) {
		super(message);
	}

	public BlockExecutionException(Throwable cause) {
		super(cause);
	}

	public BlockExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockExecutionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
