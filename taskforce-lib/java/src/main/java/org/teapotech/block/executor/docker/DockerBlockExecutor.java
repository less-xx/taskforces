/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext.ContainerSettings;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext.ExecutionConfig;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.taskforce.task.TaskExecutionUtil;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerExit;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;

/**
 * @author jiangl
 *
 */
public class DockerBlockExecutor extends AbstractBlockExecutor {

	private ExecutorService executorService;
	private final DockerBlockManager dockerBlockManager;
	protected final DockerClient dockerClient;

	/**
	 * @param block
	 */
	public DockerBlockExecutor(Block block, DockerClient dockerClient) {
		super(block);
		this.dockerBlockManager = new DockerBlockManager(dockerClient);
		this.dockerClient = dockerClient;
	}

	/**
	 * @param blockValue
	 */
	public DockerBlockExecutor(BlockValue blockValue, DockerClient dockerClient) {
		super(blockValue);
		this.dockerBlockManager = new DockerBlockManager(dockerClient);
		this.dockerClient = dockerClient;
	}

	@Override
	final protected Object doExecute(BlockExecutionContext context) throws Exception {
		if (!(context instanceof DockerBlockExecutionContext)) {
			throw new BlockExecutionException(
					"The context type is " + context.getClass() + ", it should be "
							+ DockerBlockExecutionContext.class);
		}
		String blockType = this.block.getType();

		DockerBlockDescriptor td = this.dockerBlockManager.getTaskDescriptorByName(blockType + ":active");
		if (td == null) {
			throw new BlockExecutionException("Cannot find task " + blockType);
		}
		DockerTaskExecutionResult result = executeDockerBlock((DockerBlockExecutionContext) context, td);
		if (result.getOutputValue() != null) {
			return result.getOutputValue();
		}
		if (result.getExitCode() != 0) {
			LOG.error("Error happen when execute the block. \n{}", result);
			Object logs = context.getVariable(result.getStorageKey() + ".log");
			LOG.error("Block execution logs: \n{}", logs);
		}
		return null;
	}

	protected DockerTaskExecutionResult executeDockerBlock(DockerBlockExecutionContext context,
			DockerBlockDescriptor descriptor) {

		ExecutionConfig execConf = context.getExecutionConfig();
		executorService = Executors.newFixedThreadPool(execConf.getTaskWorkerNumber());
		LOG.info("Initialized task workers, count={}", execConf.getTaskWorkerNumber());
		DockerTaskExecutionResult result = new DockerTaskExecutionResult();
		result.setCreatedTime(new Date());
		result.setOutputValueType(descriptor.getOutputValueType());
		final String blockKey = "block_" + RandomStringUtils.randomAlphanumeric(6);
		result.setStorageKey(blockKey);
		Future<String> runContainerTask = executorService
				.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						try {
							String img = descriptor.getName() + ":" + descriptor.getVersion();
							context.getContainerSettings().setImage(img);
							ContainerConfig cconf = createContainerConfig(blockKey, context);

							result.setStartedAt(new Date());
							final ContainerCreation creation = dockerClient.createContainer(cconf);
							final String containerId = creation.id();
							result.setContainerId(containerId);
							// context.setVariable("task.id", containerId);
							context.setVariable(blockKey, block);
							LOG.info("set variable {}=block", blockKey);

							dockerClient.startContainer(containerId);
							LOG.info("Task container started, ID: {}", containerId);
							ContainerExit exit = dockerClient.waitContainer(containerId);
							LOG.info("Container exit with code {}, ID: {}", exit.statusCode(), containerId);

							result.setExitCode(new Long(exit.statusCode()));

							ContainerInfo container = dockerClient.inspectContainer(containerId);
							result.setEndAt(container.state().finishedAt());
							result.setStartedAt(container.state().startedAt());
							result.setStatus(container.state().status());
							return containerId;
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
							result.setErrorText(e.getMessage());
							result.setExitCode(999L);
							return null;
						}
					}
				});

		try {
			String containerId = runContainerTask.get(execConf.getTaskExecutionTimeoutMinutes() * 60, TimeUnit.SECONDS);
			if (containerId == null) {
				LOG.error("Container was not created successfully.");
			} else {
				LOG.info("Container exited, ID: {}", containerId);
				if (result.getExitCode() != 0) {
					LOG.info("\n==== Block execution logs. Block ID: {}, storage key: {} ====\n", block.getId(),
							blockKey);
					final String logs;
					try (LogStream stream = dockerClient.logs(containerId, LogsParam.stdout(), LogsParam.stderr())) {
						logs = stream.readFully();
						LOG.info(logs);
					}
					result.setLogs(logs);
				}

				String outputKey = blockKey + ".output";
				LOG.info("get result from variable, key={}", outputKey);
				result.setOutputValue(context.getVariable(outputKey));
				dockerClient.removeContainer(containerId);
				LOG.info("Container removed, ID: {}", containerId);
			}
		} catch (Exception e) {
			result.setErrorText(e.getMessage());
			result.setEndAt(new Date());
			if (result.getContainerId() != null) {
				try {
					dockerClient.killContainer(result.getContainerId());
				} catch (Exception de) {
					LOG.error(de.getMessage());
				} finally {
					try {
						dockerClient.removeContainer(result.getContainerId());
					} catch (Exception e1) {
						LOG.error(e1.getMessage());
					}
				}
				LOG.info("Container killed, ID: {}", result.getContainerId());
			}
		}
		return result;
	}

	private ContainerConfig createContainerConfig(String blockKey, DockerBlockExecutionContext context) {

		ContainerSettings csetting = context.getContainerSettings();
		csetting.getLabels().put("taskforce.id", context.getWorkspaceId());
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_TASKFORICE_ID, context.getWorkspaceId());
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_TASK_BLOCK_KEY, blockKey);
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_TASK_EXEC_DRIVER,
				TaskExecutionUtil.TaskExecutionDriver.DOCKER.name().toLowerCase());
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_DOCKER_URL,
				TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_DOCKER_URL));
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_REDIS_HOST,
				TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_HOST));
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_REDIS_PORT,
				TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_PORT));
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_REDIS_PASSWORD,
				TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_PASSWORD));
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_REDIS_DATABASE,
				TaskExecutionUtil.getEnv(TaskExecutionUtil.ENV_REDIS_DATABASE));

		List<String> env = csetting.getEnvironment().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.toList());
		String networkMode = csetting.getNetworkMode().name().toLowerCase();
		final HostConfig hostConfig = HostConfig.builder().networkMode(networkMode)
				.binds(csetting.getBinds()).build();
		final ContainerConfig containerConfig = ContainerConfig.builder().image(csetting.getImage())
				.hostConfig(hostConfig)
				.env(env)
				.labels(csetting.getLabels())
				.cmd(csetting.getCommands()).build();
		return containerConfig;
	}

}
