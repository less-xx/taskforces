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
public class RedisTaskforceStorageProvider implements TaskforceStorageProvider {

	private static final Logger LOG = LoggerFactory.getLogger(RedisTaskforceStorageProvider.class);

	private final RedisTemplate<String, Object> redisTemplate;
	private String taskforceId;

	public RedisTaskforceStorageProvider(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object get(String key) {
		Object value = redisTemplate.boundHashOps(taskforceId).get(key);
		LOG.info("get value, key={}, taskforceId: {}", key, taskforceId);
		return value;
	}

	@Override
	public void put(String key, Object value) {
		redisTemplate.boundHashOps(taskforceId).put(key, value);
		LOG.info("set value, key={}, taskforceId: {}", key, taskforceId);
	}

	@Override
	public void setTaskforceId(String taskforceId) {
		this.taskforceId = taskforceId;
	}

	@Override
	public void remove(String key) {
		redisTemplate.boundHashOps(taskforceId).delete(key);
		LOG.info("delete value, key={}, taskforceId: {}", key, taskforceId);
	}

	@Override
	public Collection<String> getAllKeys() {
		return redisTemplate.boundHashOps(taskforceId).keys().stream()
				.map(k -> (String) k).collect(Collectors.toSet());
	}
}
