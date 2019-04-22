/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.DefaultBlockExecutionContext;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author lessdev
 *
 */
@Configuration
@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "default", matchIfMissing = true)
public class TaskForceContextAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskForceContextAutoConfig.class);
	public static final String CONF_TASKFORCE_ID = "${taskforce.id}";

	@Value(CONF_TASKFORCE_ID)
	String taskforceId;

	@Autowired
	TaskforceStorageProvider storageProvider;

	@Bean
	BlockExecutionContext taskForceContext() {
		DefaultBlockExecutionContext ctx = new DefaultBlockExecutionContext(taskforceId);
		taskforceId = ctx.getWorkspaceId();
		LOG.info("Taskforce id: {}", taskforceId);
		storageProvider.setTaskforceId(taskforceId);
		ctx.setStorageProvider(storageProvider);
		LOG.info("Taskforce storage provider: {}", storageProvider.getClass());
		return ctx;
	}

}
