/**
 * 
 */
package org.teapotech.taskforce.task;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.taskforce.task.TaskExecutionUtil.TaskExecutionDriver;

/**
 * @author jiangl
 *
 */
public abstract class AbstractTaskLoader implements CommandLineRunner {

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public void run(String... args) throws Exception {
		TaskExecutionDriver driver = TaskExecutionUtil.getTaskExecutionDriver();
		if (driver == TaskExecutionDriver.DOCKER) {

			String blockKey = TaskExecutionUtil.getBlockKey();
			if (StringUtils.isBlank(blockKey)) {
				LOG.error("Missing block key.");
				throw new BlockExecutionException("Missing block key.");
			}
			BlockExecutionContext context = getBlockExecutionContext();
			Block block = (Block) context.getVariable(blockKey);
			if (block == null) {
				LOG.error("Cannot find block by key: " + blockKey);
				throw new BlockExecutionException("Cannot find block by key: " + blockKey);
			}
			if (getTaskforceId() == null) {
				LOG.error("Cannot find taskforce id.");
				throw new BlockExecutionException("Cannot find taskforce id.");
			}
			try {
				Object output = execute(block, getDockerBlockRunnable());
				if (output != null) {
					context.setVariable(blockKey + ".output", output);
				}
			} finally {
				File logFile = getLogFile();
				if (!logFile.exists()) {
					LOG.warn("Cannot find log file at {}", logFile.getAbsolutePath());
				} else {
					String logContent = FileUtils.readFileToString(logFile, "UTF-8");
					context.setVariable(blockKey + ".log", logContent);
				}
			}

		} else {
			execute(args);
		}

	}

	protected String getTaskforceId() {
		return TaskExecutionUtil.getTaskforceId();
	}

	/**
	 * 
	 * @return
	 */
	abstract DockerBlockCallable getDockerBlockRunnable();

	/**
	 * 
	 * @return
	 */
	abstract BlockExecutionContext getBlockExecutionContext();

	/**
	 * 
	 * @param block
	 * @param runnable
	 * @return
	 * @throws BlockExecutionException
	 * @throws BlockExecutorNotFoundException
	 * @throws InvalidBlockException
	 */
	abstract Object execute(Block block, DockerBlockCallable runnable)
			throws BlockExecutionException, BlockExecutorNotFoundException, InvalidBlockExecutorException,
			InvalidBlockException;

	/**
	 * 
	 * @param args
	 */
	abstract void execute(String... args);

	/**
	 * 
	 * @return
	 */
	abstract File getLogFile();
}
