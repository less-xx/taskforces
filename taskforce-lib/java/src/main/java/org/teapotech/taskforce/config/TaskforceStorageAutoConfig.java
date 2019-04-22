/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
	@ConditionalOnProperty(name = "taskforce.storage-provider", havingValue = "org.teapotech.taskforce.provider.InMemoryTaskforceStorageProvider", matchIfMissing = false)
	public static class InMemoryTaskforceStorageConfig {

		@Bean()
		TaskforceStorageProvider taskforceResultStorageProvider() {
			InMemoryTaskforceStorageProvider sp = new InMemoryTaskforceStorageProvider();
			LOG.info("Taskforce result storage provider using {}", sp.getClass());
			return sp;
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "taskforce.storage-provider", havingValue = "org.teapotech.taskforce.provider.RedisTaskforceStorageProvider", matchIfMissing = false)
	public static class RedisTaskforceStorageConfig {

		@Bean
		public JedisConnectionFactory redisConnectionFactory() {
			return new JedisConnectionFactory();
		}

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
		TaskforceStorageProvider taskforceResultStorageProvider(RedisConnectionFactory redisConnectionFactory) {
			RedisTaskforceStorageProvider sp = new RedisTaskforceStorageProvider(redisTemplate(redisConnectionFactory));
			LOG.info("Taskforce result storage provider using {}", sp.getClass());
			return sp;
		}

	}
}
