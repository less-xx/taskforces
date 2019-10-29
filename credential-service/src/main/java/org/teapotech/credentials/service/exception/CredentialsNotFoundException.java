package org.teapotech.credentials.service.exception;

public class CredentialsNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6388847383633712990L;

	public CredentialsNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CredentialsNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CredentialsNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CredentialsNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CredentialsNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
