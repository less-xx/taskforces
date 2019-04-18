/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author jiangl
 *
 */
public class RedisTaskforceStorageProvider implements TaskforceStorageProvider {

	private final RedisTemplate<String, Object> redisTemplate;
	private String taskforceId;

	public RedisTaskforceStorageProvider(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object get(String key) {
		return redisTemplate.boundHashOps(taskforceId).get(key);
	}

	@Override
	public void put(String key, Object value) {
		redisTemplate.boundHashOps(taskforceId).put(key, value);
	}

	@Override
	public void setTaskforceId(String taskforceId) {
		this.taskforceId = taskforceId;
	}

	@Override
	public Collection<String> getAllKeys() {
		return redisTemplate.boundHashOps(taskforceId).keys().stream()
				.map(k -> (String) k).collect(Collectors.toSet());
	}
}
