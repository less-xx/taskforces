/**
 * 
 */
package org.teapotech.taskforce.provider;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author jiangl
 *
 */
public class RedisTaskforceResultStorageProvider implements TaskforceResultStorageProvider {

	final RedisTemplate<String, Object> redisTemplate;
	final String taskforceId;

	public RedisTaskforceResultStorageProvider(String taskforceId, RedisTemplate<String, Object> redisTemplate) {
		this.taskforceId = taskforceId;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object getResult(String key) {
		return redisTemplate.boundHashOps(taskforceId).get(key);
	}

}
