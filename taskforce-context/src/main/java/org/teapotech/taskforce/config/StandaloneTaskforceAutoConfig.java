/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.taskforce.provider.InMemoryTaskforceResultStorageProvider;
import org.teapotech.taskforce.provider.TaskforceResultStorageProvider;

/**
 * @author jiangl
 *
 */
@Configuration
@ConditionalOnProperty(name = "taskforce.active", havingValue = "false", matchIfMissing = false)
public class StandaloneTaskforceAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(StandaloneTaskforceAutoConfig.class);

	@Bean()
	TaskforceResultStorageProvider taskforceResultStorageProvider() {
		InMemoryTaskforceResultStorageProvider sp = new InMemoryTaskforceResultStorageProvider();
		LOG.info("Taskforce result storage provider using {}", sp.getClass());
		return sp;
	}

}