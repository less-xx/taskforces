package org.teapotech.taskforce.config.docker;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistry;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.DependencyRepository;
import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.executor.docker.DockerBlockExecutor;
import org.teapotech.block.executor.docker.DockerBlockManager;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.taskforce.event.BlockEventListenerFactory;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

import com.spotify.docker.client.DockerClient;

@Configuration
@EnableConfigurationProperties(TaskforceExecutionDockerDriverProperties.class)
@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "docker", matchIfMissing = false)
public class TaskforceDockerExecutorAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceDockerExecutorAutoConfig.class);

	@Autowired
	DockerClient dockerClient;

	@Autowired
	KeyValueStorageProvider storageProvider;

	@Autowired
	CustomResourcePathLoader customResourcePathLoader;

	@Autowired
	BlockEventListenerFactory blockEventListenerFactory;

	@Bean
	DependencyRepository dependencyRepository() {
		DependencyRepository dr = new DependencyRepository();
		dr.setDockerClient(dockerClient);
		return dr;
	}

	@Bean
	BlockExecutorFactory blockExecutorFactory() throws InvalidBlockException {
		BlockExecutorFactory fac = BlockExecutorFactory.build(blockRegistryManager(), dependencyRepository(),
				blockEventListenerFactory);
		LOG.info("BlockExecutorFactory initialized.");
		return fac;
	}

	@Bean
	DockerBlockManager dockerTaskManager() {
		return new DockerBlockManager(dockerClient);
	}

	@Bean("blockRegistryManager")
	BlockRegistryManager blockRegistryManager() {
		BlockRegistryManager manager = new BlockRegistryManager();
		manager.setCustomResourcePathLoader(customResourcePathLoader);
		manager.loadBlockRegistries();
		try {
			Collection<DockerBlockDescriptor> blockDescriptors = dockerTaskManager().getAllTaskDescriptors();
			for (DockerBlockDescriptor bd : blockDescriptors) {
				String name = bd.getName();
				BlockRegistry br = new BlockRegistry();
				br.setType(name);
				br.setCategory(bd.getCategory());
				br.setDefinition(bd.getDefinition());
				br.setExecutorClass(DockerBlockExecutor.class.getName());
				manager.registerBlock(br);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return manager;
	}
}
