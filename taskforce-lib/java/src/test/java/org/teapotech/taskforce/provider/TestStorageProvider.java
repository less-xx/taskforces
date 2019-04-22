package org.teapotech.taskforce.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.WorkspaceUtils;

public class TestStorageProvider {

	@Test
	public void testRedisStorageProvider() throws Exception {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
		JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
				jedisClientConfiguration.build());
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConFactory);
		RedisSerializer<String> keySerializer = new StringRedisSerializer();
		redisTemplate.setHashKeySerializer(keySerializer);
		redisTemplate.setKeySerializer(keySerializer);

		RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);

		redisTemplate.afterPropertiesSet();

		TaskforceStorageProvider storage = new RedisTaskforceStorageProvider(redisTemplate);
		storage.setTaskforceId("testTaskforce");

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_03.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			Block block = w.getBlock();
			String key = "testBlock";
			storage.put(key, block);

			Object value = storage.get(key);
			assertNotNull(value);
			System.out.println(value);
			storage.remove(key);

			value = storage.get(key);
			assertNull(value);
		}
	}
}
