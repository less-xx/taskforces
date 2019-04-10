/**
 * 
 */
package org.teapotech.taskforce.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.taskforce.context.DefaultTaskForceContext;
import org.teapotech.taskforce.context.TaskforceContext;

/**
 * @author lessdev
 *
 */
@Configuration
@ConditionalOnProperty(name = "taskforce.active", havingValue = "true", matchIfMissing = false)
public class TaskForceContextAutoConfig {

	@Bean
	TaskforceContext taskForceContext() {
		return new DefaultTaskForceContext();
	}

}
