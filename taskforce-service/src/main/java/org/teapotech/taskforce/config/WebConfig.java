/**
 * 
 */
package org.teapotech.taskforce.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.teapotech.base.interceptor.UserLogonInterceptor;
import org.teapotech.taskforce.task.config.TaskforceExecutionProperties;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * @author jiangl
 *
 */
@Configuration
@EnableConfigurationProperties(TaskforceExecutionProperties.class)
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	UserLogonInterceptor userLogonInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(userLogonInterceptor).addPathPatterns("/**");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(Include.NON_NULL);
		xmlMapper.setDefaultUseWrapper(false);
		xmlMapper.registerModule(new JaxbAnnotationModule());
		converters.add(new MappingJackson2XmlHttpMessageConverter(xmlMapper));

	}
}
