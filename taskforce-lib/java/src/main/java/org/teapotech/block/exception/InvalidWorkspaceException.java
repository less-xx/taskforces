package org.teapotech.block.exception;

public class InvalidWorkspaceException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6501116037048578698L;

	public InvalidWorkspaceException() {
	}

	public InvalidWorkspaceException(String message) {
		super(message);
	}

	public InvalidWorkspaceException(Throwable cause) {
		super(cause);
	}

	public InvalidWorkspaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidWorkspaceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
