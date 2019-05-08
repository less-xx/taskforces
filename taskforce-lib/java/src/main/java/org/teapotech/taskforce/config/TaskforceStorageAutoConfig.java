/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.teapotech.taskforce.provider.DiskFileStorageProvider;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.InMemoryKeyValueStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.provider.RedisKeyValueStorageProvider;

/**
 * @author jiangl
 *
 */
@Configuration
public class TaskforceStorageAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceStorageAutoConfig.class);

	@Configuration
	@ConditionalOnProperty(name = "taskforce.kv-storage.provider", havingValue = "org.teapotech.taskforce.provider.InMemoryTaskforceStorageProvider", matchIfMissing = false)
	public static class InMemoryTaskforceStorageConfig {

		@Bean()
		KeyValueStorageProvider kvStorageProvider() {
			InMemoryKeyValueStorageProvider sp = new InMemoryKeyValueStorageProvider();
			LOG.info("Taskforce result storage provider using {}", sp.getClass());
			return sp;
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "taskforce.kv-storage.provider", havingValue = "org.teapotech.taskforce.provider.RedisTaskforceStorageProvider", matchIfMissing = false)
	public static class RedisTaskforceStorageConfig {

		@Bean
		public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
			RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(redisConnectionFactory);
			RedisSerializer<String> keySerializer = new StringRedisSerializer();
			redisTemplate.setHashKeySerializer(keySerializer);
			redisTemplate.setKeySerializer(keySerializer);

			RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();
			redisTemplate.setValueSerializer(valueSerializer);
			redisTemplate.setHashValueSerializer(valueSerializer);

			redisTemplate.afterPropertiesSet();
			return redisTemplate;
		}

		@Bean()
		KeyValueStorageProvider kvStorageProvider(RedisConnectionFactory redisConnectionFactory) {
			RedisKeyValueStorageProvider sp = new RedisKeyValueStorageProvider(redisTemplate(redisConnectionFactory));
			LOG.info("Key-value storage provider using {}", sp.getClass());
			return sp;
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "taskforce.file-storage.provider", havingValue = "org.teapotech.taskforce.provider.DiskFileStorageProvider", matchIfMissing = false)
	public static class DiskFileStorageConfig {

		@Value("${taskforce.file-storage.base-dir}")
		String diskFileStorageBaseDir;

		@Bean()
		FileStorageProvider fileStorageProvider() {
			DiskFileStorageProvider sp = new DiskFileStorageProvider(diskFileStorageBaseDir);
			LOG.info("File storage provider using {}", sp.getClass());
			return sp;
		}
	}
}
