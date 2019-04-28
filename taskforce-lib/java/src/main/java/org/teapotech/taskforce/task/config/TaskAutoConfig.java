/**
 * 
 */
package org.teapotech.taskforce.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.taskforce.annotation.ConditionalOnPropertyNotEmpty;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
@Configuration
@ConditionalOnPropertyNotEmpty("taskforce.id")
public class TaskAutoConfig {

	private final static Logger LOG = LoggerFactory.getLogger(TaskAutoConfig.class);

	@Value("${taskforce.id}")
	String taskforceId;

	@Bean
	BlockExecutionContext blockExecutionContext(BlockExecutorFactory factory,
			TaskforceStorageProvider storageProvider) {
		DockerBlockExecutionContext context = new DockerBlockExecutionContext(taskforceId, factory, storageProvider);
		LOG.info("Initialized block execution context. Taskforce ID: {}", taskforceId);
		return context;
	}
}
