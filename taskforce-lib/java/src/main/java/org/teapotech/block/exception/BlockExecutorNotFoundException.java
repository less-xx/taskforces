package org.teapotech.block.exception;

public class BlockExecutorNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4076209147901991062L;

	public BlockExecutorNotFoundException() {
	}

	public BlockExecutorNotFoundException(String message) {
		super(message);
	}

	public BlockExecutorNotFoundException(Throwable cause) {
		super(cause);
	}

	public BlockExecutorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockExecutorNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
