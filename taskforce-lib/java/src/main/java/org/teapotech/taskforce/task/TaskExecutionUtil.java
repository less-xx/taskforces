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
	public final static String ENV_TASK_EXEC_MODE = "TASK_EXEC_MODE";

	private static final Logger LOG = LoggerFactory.getLogger(TaskExecutionUtil.class);

	public static enum TaskExecutionMode {
		HOST, DOCKER
	}

	public static TaskExecutionMode getTaskExecutionMode() {
		String env = System.getProperty(ENV_TASK_EXEC_MODE);
		if (env == null) {
			env = System.getenv(ENV_TASK_EXEC_MODE);
		}
		TaskExecutionMode mode = TaskExecutionMode.HOST;
		if ("docker".equalsIgnoreCase(env)) {
			mode = TaskExecutionMode.DOCKER;
		}
		LOG.info("Task execution mode: {}", mode);
		return mode;
	}

	public static String getTaskforceId() {
		String env = System.getProperty(ENV_TASKFORICE_ID);
		if (env == null) {
			env = System.getenv(ENV_TASKFORICE_ID);
		}
		return env;
	}
}
