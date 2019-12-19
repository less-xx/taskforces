/**
 * 
 */
package org.teapotech.base.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.teapotech.base.interceptor.UserLogonInterceptor;
import org.teapotech.base.service.user.User;

/**
 * @author jiangl
 *
 */
public abstract class LogonUserController {

	@Autowired
	protected HttpSession httpSession;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	final protected User getLogonUser() {
		return (User) httpSession.getAttribute(UserLogonInterceptor.SESSION_CURRENT_LOGON_USER);
	}
}
