package org.teapotech.taskforce.config.docker;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutor;
import org.teapotech.block.executor.docker.DockerTaskManager;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

import com.spotify.docker.client.DockerClient;

@Configuration
@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "docker", matchIfMissing = false)
public class TaskDockerExecutorAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskDockerExecutorAutoConfig.class);
	public static final String CONF_TASKFORCE_ID = "${taskforce.id}";

	@Value(CONF_TASKFORCE_ID)
	String taskforceId;

	@Autowired
	DockerClient dockerClient;

	@Autowired
	TaskforceStorageProvider storageProvider;;

	@Bean
	BlockExecutionContext taskForceContext() throws Exception {
		BlockExecutorFactory fac = blockExecutorFactory();
		DockerBlockExecutionContext ctx = new DockerBlockExecutionContext(taskforceId, fac);
		LOG.info("Taskforce id: {}", taskforceId);

		storageProvider.setTaskforceId(taskforceId);
		ctx.setTaskforceResultStorageProvider(storageProvider);
		LOG.info("Taskforce storage provider: {}", storageProvider.getClass());

		Collection<DockerBlockDescriptor> blockDescriptors = dockerTaskManager().getAllTaskDescriptors();
		for (DockerBlockDescriptor bd : blockDescriptors) {
			String name = bd.getName();
			fac.registerExecutor(name, DockerBlockExecutor.class);
		}
		return ctx;
	}

	@Bean
	BlockExecutorFactory blockExecutorFactory() {
		BlockExecutorFactory fac = BlockExecutorFactory.build(dockerClient);
		return fac;
	}

	@Bean
	DockerTaskManager dockerTaskManager() {
		return new DockerTaskManager(dockerClient);
	}
}
