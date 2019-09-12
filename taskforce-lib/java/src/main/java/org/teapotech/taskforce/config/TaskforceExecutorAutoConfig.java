package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.DependencyRepository;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.taskforce.event.BlockEventListenerFactory;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

@Configuration
@ConditionalOnProperty(name = "taskforce.execution.support-docker", havingValue = "false", matchIfMissing = true)
public class TaskforceExecutorAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceExecutorAutoConfig.class);

	@Autowired
	KeyValueStorageProvider storageProvider;

	@Autowired
	CustomResourcePathLoader customResourcePathLoader;

	@Autowired
	BlockEventListenerFactory blockEventListenerFactory;

	@Bean
	DependencyRepository dependencyRepository() {
		DependencyRepository dr = new DependencyRepository();
		return dr;
	}

	@Bean
	BlockExecutorFactory blockExecutorFactory() throws InvalidBlockException {
		BlockExecutorFactory fac = BlockExecutorFactory.build(blockRegistryManager(), dependencyRepository(),
				blockEventListenerFactory);
		LOG.info("BlockExecutorFactory initialized.");
		return fac;
	}


	@Bean("blockRegistryManager")
	BlockRegistryManager blockRegistryManager() {
		BlockRegistryManager manager = new BlockRegistryManager();
		manager.setCustomResourcePathLoader(customResourcePathLoader);
		manager.loadBlockRegistries();
		return manager;
	}
}
