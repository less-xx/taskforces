/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
		executeDockerBlock((DockerBlockExecutionContext) context, td);
		return null;
	}

	protected DockerTaskExecutionResult executeDockerBlock(DockerBlockExecutionContext context,
			DockerBlockDescriptor descriptor)
			throws BlockExecutionException {

		ExecutionConfig execConf = context.getExecutionConfig();
		executorService = Executors.newFixedThreadPool(execConf.getTaskWorkerNumber());
		LOG.info("Initialized task workers, count={}", execConf.getTaskWorkerNumber());
		DockerTaskExecutionResult result = new DockerTaskExecutionResult();
		result.setCreatedTime(new Date());
		result.setOutputValueType(descriptor.getOutputValueType());
		Future<String> runContainerTask = executorService
				.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						try {
							String img = descriptor.getName() + ":" + descriptor.getVersion();
							context.getContainerSettings().setImage(img);
							ContainerConfig cconf = createContainerConfig(context);

							result.setStartedAt(new Date());
							final ContainerCreation creation = dockerClient.createContainer(cconf);
							final String containerId = creation.id();
							result.setContainerId(containerId);
							context.setVariable("task.id", containerId);

							dockerClient.startContainer(containerId);
							LOG.info("Task container started, ID: {}", containerId);
							ContainerExit exit = dockerClient.waitContainer(containerId);
							LOG.info("Container exit with code {}, ID: {}", exit.statusCode(), containerId);

							final String output;
							try (LogStream stream = dockerClient.logs(containerId, LogsParam.stdout(),
									LogsParam.stderr())) {
								output = stream.readFully();
								LOG.info(output);
							}
							result.setExitCode(new Long(exit.statusCode()));
							if (result.getExitCode() == 0) {
								result.setOutputValue(output);
							} else {
								result.setErrorText(output);
							}
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
				dockerClient.removeContainer(containerId);
				LOG.info("Container removed, ID: {}", containerId);
			}
		} catch (Exception e) {
			result.setErrorText(e.getClass().getSimpleName());
			result.setErrorDescription(e.getMessage());
			result.setEndAt(new Date());
			if (result.getContainerId() != null) {
				try (LogStream stream = dockerClient.logs(result.getContainerId(), LogsParam.stdout(),
						LogsParam.stderr())) {
					String logs = stream.readFully();
					result.setLogs(logs);
					LOG.warn(logs);
					try {
						dockerClient.killContainer(result.getContainerId());
					} catch (Exception de) {
						LOG.error(de.getMessage());
					} finally {
						dockerClient.removeContainer(result.getContainerId());
					}

				} catch (Exception e1) {
					LOG.error(e1.getMessage(), e1);
				}
				LOG.info("Container killed, ID: {}", result.getContainerId());
			}
		}
		return result;
	}

	private ContainerConfig createContainerConfig(DockerBlockExecutionContext context) {

		ContainerSettings csetting = context.getContainerSettings();
		csetting.getLabels().put("taskforce.id", context.getWorkspaceId());
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_TASKFORICE_ID, context.getWorkspaceId());
		csetting.getEnvironment().put(TaskExecutionUtil.ENV_TASK_EXEC_MODE,
				TaskExecutionUtil.TaskExecutionMode.DOCKER.name());
		String networkMode = csetting.getNetworkMode().name().toLowerCase();
		final HostConfig hostConfig = HostConfig.builder().networkMode(networkMode)
				.binds(csetting.getBinds()).build();
		final ContainerConfig containerConfig = ContainerConfig.builder().image(csetting.getImage())
				.hostConfig(hostConfig)
				.labels(csetting.getLabels())
				.cmd(csetting.getCommands()).build();
		return containerConfig;
	}

}
