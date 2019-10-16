/**
 * 
 */
package org.teapotech.taskforce.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.teapotech.block.exception.InvalidWorkspaceException;
import org.teapotech.taskforce.exception.CustomResourceLocationException;
import org.teapotech.taskforce.exception.TaskforceDataStoreException;
import org.teapotech.taskforce.exception.TaskforceExecutionException;
import org.teapotech.taskforce.web.ErrorResponse;
import org.teapotech.user.exception.UserNotLogonException;

/**
 * @author jiangl
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler({ HttpMessageNotReadableException.class, InvalidWorkspaceException.class,
			TaskforceDataStoreException.class,
			TaskforceExecutionException.class, CustomResourceLocationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleTaskforceDataStoreException(Exception e, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return respondError(e, httpRequest, httpResponse, true);
	}

	@ExceptionHandler({ UserNotLogonException.class })
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResponse handleUserNotLogonException(Exception e, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return respondError(e, httpRequest, httpResponse, true);
	}

	@ExceptionHandler({ EntityNotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleEntityNotFoundException(Exception e, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return respondError(e, httpRequest, httpResponse, false);
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse handleGeneralException(Exception e, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return respondError(e, httpRequest, httpResponse, true);
	}

	private ErrorResponse respondError(Exception e,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, boolean showStackTrace) {
		return respondError(null, e, httpRequest, httpResponse, showStackTrace);
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

}
