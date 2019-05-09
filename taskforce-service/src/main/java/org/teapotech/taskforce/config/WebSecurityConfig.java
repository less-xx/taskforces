/**
 * 
 */
package org.teapotech.taskforce.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author jiangl
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final static Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Value("${security.web.allowed-origins:*}")
	String allowedOrigins;

	@Value("${security.web.allowed-methods}")
	String allowedMethods;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.cors().and().authorizeRequests().anyRequest().permitAll();
		http.csrf().disable();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		LOG.info("Allowed origins: {}", allowedOrigins);
		String[] ao = this.allowedOrigins.split("\\s*,\\s*");

		LOG.info("Allowed methods: {}", allowedMethods);
		String[] am = this.allowedMethods.split("\\s*,\\s*");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(ao));
		configuration.setAllowedMethods(Arrays.asList(am));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
