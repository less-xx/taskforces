/**
 * 
 */
package org.teapotech.taskforce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.teapotech.taskforce.context.DefaultTaskForceContext;
import org.teapotech.taskforce.context.TaskforceContext;
import org.teapotech.taskforce.provider.RedisTaskforceResultStorageProvider;
import org.teapotech.taskforce.provider.TaskforceResultStorageProvider;

/**
 * @author lessdev
 *
 */
@Configuration
@ConditionalOnProperty(name = "taskforce.active", havingValue = "true", matchIfMissing = false)
public class TaskForceContextAutoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TaskForceContextAutoConfig.class);

	@Value("${taskforce.id}")
	String taskforceId;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Bean()
	TaskforceResultStorageProvider taskforceResultStorageProvider() {
		TaskforceResultStorageProvider sp = new RedisTaskforceResultStorageProvider(taskforceId, redisTemplate);
		LOG.info("Taskforce result storage provider using {}", sp.getClass());
		return sp;
	}

	@Bean
	TaskforceContext taskForceContext() {
		DefaultTaskForceContext ctx = new DefaultTaskForceContext(taskforceId);
		LOG.info("Taskforce id {}", ctx.getTaskforceId());
		ctx.setTaskforceResultStorageProvider(taskforceResultStorageProvider());
		return ctx;
	}

}
