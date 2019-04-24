/**
 * 
 */
package org.teapotech.taskforce.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangl
 *
 */
public class TaskExecutionUtil {

	public final static String ENV_TASKFORICE_ID = "TASKFORCE_ID";
	public final static String ENV_TASK_EXEC_DRIVER = "TASK_EXEC_DRIVER";
	public final static String ENV_TASK_BLOCK_KEY = "TASK_BLOCK_KEY";
	public final static String ENV_REDIS_HOST = "CONFIG_REDIS_HOST";
	public final static String ENV_REDIS_PORT = "CONFIG_REDIS_PORT";
	public final static String ENV_REDIS_PASSWORD = "CONFIG_REDIS_PASSWORD";
	public final static String ENV_REDIS_DATABASE = "CONFIG_REDIS_DATABASE";
	public final static String ENV_DOCKER_URL = "DOCKER_URL";

	private static final Logger LOG = LoggerFactory.getLogger(TaskExecutionUtil.class);

	public static enum TaskExecutionDriver {
		DEFAULT, DOCKER
	}

	public static String getEnv(String property) {
		String env = System.getProperty(property);
		if (env == null) {
			env = System.getenv(property);
		}
		return env;
	}

	public static TaskExecutionDriver getTaskExecutionDriver() {
		String env = getEnv(ENV_TASK_EXEC_DRIVER);
		TaskExecutionDriver driver = TaskExecutionDriver.DEFAULT;
		if ("docker".equalsIgnoreCase(env)) {
			driver = TaskExecutionDriver.DOCKER;
		}
		LOG.info("Task execution mode: {}", driver);
		return driver;
	}

	public static String getTaskforceId() {
		return getEnv(ENV_TASKFORICE_ID);
	}

	public static String getBlockKey() {
		return getEnv(ENV_TASK_BLOCK_KEY);
	}
}
