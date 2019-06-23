package org.teapotech.taskforce.exception;

public class TaskforceExecutionException extends Exception {

	private static final long serialVersionUID = 595478354215319622L;

	public TaskforceExecutionException() {
	}

	public TaskforceExecutionException(String message) {
		super(message);
	}

	public TaskforceExecutionException(Throwable cause) {
		super(cause);
	}

	public TaskforceExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskforceExecutionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
