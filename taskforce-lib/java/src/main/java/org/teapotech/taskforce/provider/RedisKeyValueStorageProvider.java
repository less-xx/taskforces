/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author jiangl
 *
 */
public class RedisKeyValueStorageProvider implements KeyValueStorageProvider {

	private static final Logger LOG = LoggerFactory.getLogger(RedisKeyValueStorageProvider.class);

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisKeyValueStorageProvider(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object get(String taskforceId, String key) {
		Object value = redisTemplate.boundHashOps(taskforceId).get(key);
		LOG.info("get value, key={}, taskforceId: {}", key, taskforceId);
		return value;
	}

	@Override
	public void put(String taskforceId, String key, Object value) {
		redisTemplate.boundHashOps(taskforceId).put(key, value);
		LOG.info("set value, key={}, taskforceId: {}", key, taskforceId);
	}

	@Override
	public void remove(String taskforceId, String key) {
		redisTemplate.boundHashOps(taskforceId).delete(key);
		LOG.info("delete value, key={}, taskforceId: {}", key, taskforceId);
	}

	@Override
	public Collection<String> getAllKeys(String taskforceId) {
		return redisTemplate.boundHashOps(taskforceId).keys().stream()
				.map(k -> (String) k).collect(Collectors.toSet());
	}

	@Override
	public void destroy(String taskforceId) {
		redisTemplate.delete(taskforceId);
		LOG.info("delete all values by taskforaceId: {}", taskforceId);
	}
}
