/**
 * 
 */
package org.teapotech.taskforce.web;

/**
 * @author jiangl
 *
 */
public class ErrorResponse {
	String message;

	public ErrorResponse() {
	}

	public ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
