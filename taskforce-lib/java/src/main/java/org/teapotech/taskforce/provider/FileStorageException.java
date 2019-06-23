package org.teapotech.taskforce.provider;

public class FileStorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -898449801461453442L;

	public FileStorageException() {
	}

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(Throwable cause) {
		super(cause);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileStorageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
