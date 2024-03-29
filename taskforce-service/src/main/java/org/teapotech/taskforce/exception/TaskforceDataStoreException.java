package org.teapotech.taskforce.exception;

public class TaskforceDataStoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7049601105944493437L;

	public TaskforceDataStoreException() {
	}

	public TaskforceDataStoreException(String message) {
		super(message);
	}

	public TaskforceDataStoreException(Throwable cause) {
		super(cause);
	}

	public TaskforceDataStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskforceDataStoreException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
