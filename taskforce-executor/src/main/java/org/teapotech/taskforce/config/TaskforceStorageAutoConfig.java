/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.teapotech.taskforce.provider.InMemoryTaskforceStorageProvider;
import org.teapotech.taskforce.provider.RedisTaskforceStorageProvider;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
@Configuration
public class TaskforceStorageAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceStorageAutoConfig.class);

	@Configuration
	@ConditionalOnProperty(name = "taskforce.storage-provider", havingValue = "org.teapotech.taskforce.provider.InMemoryTaskforceResultStorageProvider", matchIfMissing = false)
	public static class InMemoryTaskforceStorageConfig {

		@Bean()
		TaskforceStorageProvider taskforceResultStorageProvider() {
			InMemoryTaskforceStorageProvider sp = new InMemoryTaskforceStorageProvider();
			LOG.info("Taskforce result storage provider using {}", sp.getClass());
			return sp;
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "taskforce.storage-provider", havingValue = "org.teapotech.taskforce.provider.RedisTaskforceResultStorageProvider", matchIfMissing = false)
	public static class RedisTaskforceStorageConfig {

		@Autowired
		RedisTemplate<String, Object> redisTemplate;

		@Bean()
		TaskforceStorageProvider taskforceResultStorageProvider() {
			RedisTaskforceStorageProvider sp = new RedisTaskforceStorageProvider(redisTemplate);
			LOG.info("Taskforce result storage provider using {}", sp.getClass());
			return sp;
		}

	}
}
