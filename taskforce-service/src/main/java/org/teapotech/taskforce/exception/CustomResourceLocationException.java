package org.teapotech.taskforce.exception;

public class CustomResourceLocationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3148866942125202363L;

	public CustomResourceLocationException() {
	}

	public CustomResourceLocationException(String message) {
		super(message);
	}

	public CustomResourceLocationException(Throwable cause) {
		super(cause);
	}

	public CustomResourceLocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomResourceLocationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
