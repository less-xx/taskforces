package org.teapotech.taskforce.credential.oauth2;

public class InvalidOAuth2ConigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308232900352315403L;

	public InvalidOAuth2ConigurationException() {
	}

	public InvalidOAuth2ConigurationException(String message) {
		super(message);
	}

	public InvalidOAuth2ConigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidOAuth2ConigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOAuth2ConigurationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
