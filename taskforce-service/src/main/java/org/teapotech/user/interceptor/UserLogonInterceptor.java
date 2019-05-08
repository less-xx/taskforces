/**
 * 
 */
package org.teapotech.user.interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.teapotech.taskforce.entity.DefaultUserImpl;
import org.teapotech.user.User;
import org.teapotech.user.UserService;
import org.teapotech.user.exception.UserNotLogonException;

/**
 * @author jiangl
 *
 */
@Component
public class UserLogonInterceptor implements HandlerInterceptor {

	public final static String SESSION_CURRENT_LOGON_USER = "CURRENT_LOGON_USER";

	@Value("${application.user.logon-required}")
	private boolean logonRequired;

	@Value("${application.user.default-userid}")
	private String defaultUserId;

	@Value("${application.user.default-username}")
	private String defaultUserName;

	private User defaultUser = null;

	@Autowired
	UserService userService;

	@PostConstruct
	void init() {
		DefaultUserImpl du = new DefaultUserImpl();
		du.setId(defaultUserId);
		du.setName(defaultUserName);
		this.defaultUser = du;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		User logonUser = (User) request.getSession().getAttribute(SESSION_CURRENT_LOGON_USER);
		if (logonUser == null) {
			if (logonRequired) {
				throw new UserNotLogonException("User not logon");
			} else {
				logonUser = this.defaultUser;
				request.getSession().setAttribute(SESSION_CURRENT_LOGON_USER, this.defaultUser);
			}
		}
		userService.userDidLogon(logonUser);
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
