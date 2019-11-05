/**
 * 
 */
package org.teapotech.credentials.conf;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.credentials.Oauth2Credentials;
import org.teapotech.credentials.util.OAuth2CredentialDeserializer;
import org.teapotech.util.JsonHelper;

/**
 * @author jiangl
 *
 */
@Configuration
@EnableConfigurationProperties(CredentialsServiceConfigurationProperties.class)
public class CredentialsServiceConfig {

	private static Logger LOG = LoggerFactory.getLogger(CredentialsServiceConfig.class);

	@Autowired
	CredentialsServiceConfigurationProperties configProps;

	@Bean("credential-service-home-dir")
	File getHomeDir() {
		File home = new File(configProps.getHomeDir());
		if (!home.exists()) {
			home.mkdirs();
			LOG.info("Create home directory: {}", home.getAbsolutePath());
		} else {
			LOG.info("Home directory: {}", home.getAbsolutePath());
		}
		return home;
	}

	@Bean
	@ConditionalOnMissingBean
	JsonHelper jsonHelper() {

		return JsonHelper.newInstance().addDeserializer(Oauth2Credentials.class, new OAuth2CredentialDeserializer())
				.build();
	}
}
