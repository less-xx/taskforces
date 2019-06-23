package org.teapotech.block.exception;

public class BlockExecutionContextException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 737550822236053249L;

	public BlockExecutionContextException() {
	}

	public BlockExecutionContextException(String message) {
		super(message);
	}

	public BlockExecutionContextException(Throwable cause) {
		super(cause);
	}

	public BlockExecutionContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockExecutionContextException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
