/**
 * 
 */
package org.teapotech.taskforce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.teapotech.user.interceptor.UserLogonInterceptor;

/**
 * @author jiangl
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	UserLogonInterceptor userLogonInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(userLogonInterceptor).addPathPatterns("/**");
	}
}
