/**
 * 
 */
package org.teapotech.taskforce.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.taskforce.annotation.ConditionalOnPropertyNotEmpty;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

/**
 * @author jiangl
 *
 */
@Configuration
@ConditionalOnPropertyNotEmpty("taskforce.id")
@EnableConfigurationProperties(TaskforceExecutionProperties.class)
public class TaskforceAutoConfig {

	private final static Logger LOG = LoggerFactory.getLogger(TaskforceAutoConfig.class);

	@Value("${taskforce.id}")
	String taskforceId;

	@Autowired
	EventDispatcher eventDispatcher;

	@Autowired
	TaskforceExecutionProperties executionProperties;

	@Bean
	BlockExecutionContext blockExecutionContext(BlockExecutorFactory factory,
			KeyValueStorageProvider kvStorageProvider,
			FileStorageProvider fileStorageProvider) {
		DockerBlockExecutionContext context = new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider,
				fileStorageProvider, eventDispatcher, executionProperties);
		LOG.info("Initialized block execution context. Taskforce ID: {}", taskforceId);
		return context;
	}
}
