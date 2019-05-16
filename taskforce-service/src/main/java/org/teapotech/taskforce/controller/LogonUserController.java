/**
 * 
 */
package org.teapotech.taskforce.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.user.User;
import org.teapotech.user.interceptor.UserLogonInterceptor;

/**
 * @author jiangl
 *
 */
public abstract class LogonUserController {

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	final protected User getLogonUser(HttpServletRequest httpRequest) {
		return (User) httpRequest.getSession().getAttribute(UserLogonInterceptor.SESSION_CURRENT_LOGON_USER);
	}
}
