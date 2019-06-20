/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.exception.BlockExecutionContextException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageException;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

/**
 * @author jiangl
 *
 */
public class DockerBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private final KeyValueStorageProvider kvStorageProvider;
	private final FileStorageProvider fileStorageProvider;
	private final BlockEventDispatcher blockEventDispatcher;
	private final WorkspaceEventDispatcher workspaceEventDispatcher;
	private final ContainerSettings containerSettings = new ContainerSettings();
	private final ExecutionConfig executionConfig = new ExecutionConfig();
	private boolean stopped;

	public DockerBlockExecutionContext(String workspaceId, BlockExecutorFactory factory,
			KeyValueStorageProvider kvStorageProvider,
			FileStorageProvider fileStorageProvider,
			BlockEventDispatcher blockEventDispatcher,
			WorkspaceEventDispatcher workspaceEventDispatcher) {
		this.blockExecutorFactory = factory;
		this.workspaceId = workspaceId;
		this.kvStorageProvider = kvStorageProvider;
		this.fileStorageProvider = fileStorageProvider;
		this.blockEventDispatcher = blockEventDispatcher;
		this.workspaceEventDispatcher = workspaceEventDispatcher;
	}

	@Override
	public String getWorkspaceId() {
		return workspaceId;
	}

	public KeyValueStorageProvider getKeyValueStorageProvider() {
		return kvStorageProvider;
	}

	public FileStorageProvider getFileStorageProvider() {
		return fileStorageProvider;
	}

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public void setVariable(String id, Object value) {
		if (kvStorageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		kvStorageProvider.put(workspaceId, id, value);
	}

	@Override
	public Collection<String> getAllVariableNames() {
		if (kvStorageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		return kvStorageProvider.getAllKeys(workspaceId);
	}

	@Override
	public Object getVariable(String id) {
		if (kvStorageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		return kvStorageProvider.get(workspaceId, id);
	}

	@Override
	public void destroy() {
		if (kvStorageProvider == null) {
			throw new BlockExecutionContextException("TaskforceStorageProvider is not configured.");
		}
		kvStorageProvider.destroy(workspaceId);
	}

	@Override
	public void storeFile(String key, InputStream in) throws FileStorageException {
		this.fileStorageProvider.store(workspaceId, key, in);

	}

	@Override
	public InputStream loadFile(String key) throws FileStorageException {
		return this.fileStorageProvider.load(workspaceId, key);
	}

	@Override
	public BlockEventDispatcher getBlockEventDispatcher() {
		return this.blockEventDispatcher;
	}

	public ContainerSettings getContainerSettings() {
		return containerSettings;
	}

	public ExecutionConfig getExecutionConfig() {
		return executionConfig;
	}

	@Override
	public WorkspaceEventDispatcher getWorkspaceEventDispatcher() {
		return this.workspaceEventDispatcher;
	}

	@Override
	public boolean isStopped() {
		return this.stopped;
	}

	@Override
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
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
