/**
 * 
 */
package org.teapotech.taskforce.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.taskforce.annotation.ConditionalOnPropertyNotEmpty;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

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

	@Autowired
	BlockEventDispatcher blockEventDispatcher;

	@Autowired
	WorkspaceEventDispatcher workspaceEventDispatcher;

	@Bean
	BlockExecutionContext blockExecutionContext(BlockExecutorFactory factory,
			KeyValueStorageProvider kvStorageProvider,
			FileStorageProvider fileStorageProvider) {
		DockerBlockExecutionContext context = new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider,
				fileStorageProvider, blockEventDispatcher, workspaceEventDispatcher);
		LOG.info("Initialized block execution context. Taskforce ID: {}", taskforceId);
		return context;
	}
}
