package org.teapotech.taskforce.exception;

public class InvalidTaskforceStorageConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4732444964123335416L;

	public InvalidTaskforceStorageConfigurationException() {
	}

	public InvalidTaskforceStorageConfigurationException(String message) {
		super(message);
	}

	public InvalidTaskforceStorageConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidTaskforceStorageConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTaskforceStorageConfigurationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
