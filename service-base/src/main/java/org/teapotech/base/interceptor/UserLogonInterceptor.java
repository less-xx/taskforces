/**
 * 
 */
package org.teapotech.base.interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.teapotech.base.exception.UserNotLogonException;
import org.teapotech.base.service.user.User;
import org.teapotech.base.service.user.UserService;

/**
 * @author jiangl
 *
 */
@Component
public class UserLogonInterceptor implements HandlerInterceptor {
	protected Logger LOG = LoggerFactory.getLogger(getClass());
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
		this.defaultUser = new User() {

			@Override
			public String getName() {
				return defaultUserId;
			}

			@Override
			public String getId() {
				return defaultUserName;
			}
		};
		LOG.info("Default user initiated, id: {}, name: {}", this.defaultUser.getId(), this.defaultUser.getName());
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
