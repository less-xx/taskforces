/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.exception.BlockExecutionContextException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
public class DockerBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private TaskforceStorageProvider storageProvider;
	private final ContainerSettings containerSettings = new ContainerSettings();
	private final ExecutionConfig executionConfig = new ExecutionConfig();

	public DockerBlockExecutionContext(String workspaceId, BlockExecutorFactory factory) {
		this.blockExecutorFactory = factory;
		this.workspaceId = workspaceId;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public TaskforceStorageProvider getTaskforceResultStorageProvider() {
		return storageProvider;
	}

	public void setTaskforceResultStorageProvider(TaskforceStorageProvider storageProvider) {
		this.storageProvider = storageProvider;
	}

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public void setVariable(String id, Object value) {
		if (storageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		storageProvider.put(id, value);
	}

	@Override
	public Collection<String> getAllVariableNames() {
		if (storageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		return storageProvider.getAllKeys();
	}

	@Override
	public Object getVariable(String id) {
		if (storageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		return storageProvider.get(id);
	}

	public ContainerSettings getContainerSettings() {
		return containerSettings;
	}

	public ExecutionConfig getExecutionConfig() {
		return executionConfig;
	}

	/**
	 * 
	 * @author jiangl
	 *
	 */
	public static class ContainerSettings {
		private final Map<String, String> environment = new HashMap<>();
		private final List<String> commands = new ArrayList<>();
		private final List<String> binds = new ArrayList<>();
		private NetworkMode networkMode = NetworkMode.BRIDGE;
		private String image;
		private final Map<String, String> labels = new HashMap<>();

		public Map<String, String> getEnvironment() {
			return environment;
		}

		public List<String> getCommands() {
			return commands;
		}

		public List<String> getBinds() {
			return binds;
		}

		public void setNetworkMode(NetworkMode networkMode) {
			this.networkMode = networkMode;
		}

		public NetworkMode getNetworkMode() {
			return networkMode;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public Map<String, String> getLabels() {
			return labels;
		}

		/**
		 * 
		 * @author jiangl
		 *
		 */
		public static enum NetworkMode {
			NONE, BRIDGE, HOST
		}
	}

	/**
	 * 
	 * @author jiangl
	 *
	 */
	public static class ExecutionConfig {
		int taskExecutionTimeoutMinutes = 60;
		int taskWorkerNumber = 3;

		public int getTaskExecutionTimeoutMinutes() {
			return taskExecutionTimeoutMinutes;
		}

		public void setTaskExecutionTimeoutMinutes(int taskExecutionTimeoutMinutes) {
			this.taskExecutionTimeoutMinutes = taskExecutionTimeoutMinutes;
		}

		public int getTaskWorkerNumber() {
			return taskWorkerNumber;
		}

		public void setTaskWorkerNumber(int taskWorkerNumber) {
			this.taskWorkerNumber = taskWorkerNumber;
		}
	}
}
