/**
 * 
 */
package org.teapotech.taskforce.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jiangl
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse handleGeneralException(Exception e, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return respondError(null, e, httpRequest, httpResponse, true);
	}

	private ErrorResponse respondError(String message, Exception e,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, boolean showStackTrace) {
		if (showStackTrace) {
			LOG.error(e.getMessage(), e);
		} else {
			LOG.error(e.getClass() + ": " + e.getMessage());
		}

		String errorMsg = message != null ? message : cleanMessage(e.getMessage());
		return new ErrorResponse(errorMsg);
	}

	private String cleanMessage(String input) {
		return input.replaceAll("(<.*?>)|(&.*?;)|([ ]{2,})", "");
	}

	public static class ErrorResponse {

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
}
