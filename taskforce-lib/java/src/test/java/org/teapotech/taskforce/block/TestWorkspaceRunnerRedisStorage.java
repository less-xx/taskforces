/**
 * 
 */
package org.teapotech.taskforce.block;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.DefaultBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.block.util.WorkspaceUtils;
import org.teapotech.taskforce.provider.RedisTaskforceStorageProvider;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;
import org.teapotech.taskforce.task.TaskExecutionUtil;

/**
 * @author jiangl
 *
 */
public class TestWorkspaceRunnerRedisStorage {

	private static BlockExecutorFactory factory;
	private static TaskforceStorageProvider storageProvider;

	@BeforeAll
	static void init() {
		factory = BlockExecutorFactory.build();
		String host = TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_HOST);
		String port = TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_PORT);
		String password = TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_PASSWORD);
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host,
				Integer.parseInt(port));
		redisStandaloneConfiguration.setPassword(password);
		redisStandaloneConfiguration.setDatabase(1);
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

		storageProvider = new RedisTaskforceStorageProvider(redisTemplate);
	}

	@Test
	public void testRunWorkspace_01() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setStorageProvider(storageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);
		}
	}

	@Test
	public void testRunWorkspace_02() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setStorageProvider(storageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);
		}
	}

	@Test
	public void testRunWorkspace_04() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_04.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setStorageProvider(storageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);

			for (String name : context.getAllVariableNames()) {
				Object value = context.getVariable(name);
				System.out.println(name + "=" + value);
			}
		}
	}

}
