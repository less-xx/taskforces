/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.taskforce.context.DefaultTaskForceContext;
import org.teapotech.taskforce.context.TaskforceContext;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author lessdev
 *
 */
@Configuration
public class TaskForceContextAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskForceContextAutoConfig.class);
	public static final String CONF_TASKFORCE_ID = "${taskforce.id}";

	@Value(CONF_TASKFORCE_ID)
	String taskforceId;

	@Autowired
	TaskforceStorageProvider storageProvider;

	@Bean
	TaskforceContext taskForceContext() {
		DefaultTaskForceContext ctx = new DefaultTaskForceContext(taskforceId);
		taskforceId = ctx.getTaskforceId();
		LOG.info("Taskforce id: {}", taskforceId);
		storageProvider.setTaskforceId(taskforceId);
		ctx.setTaskforceResultStorageProvider(storageProvider);
		LOG.info("Taskforce storage provider: {}", storageProvider.getClass());
		return ctx;
	}

}
