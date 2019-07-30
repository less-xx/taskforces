/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.exception.BlockExecutionContextException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.provider.FileStorageException;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.task.config.TaskforceExecutionProperties;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;

/**
 * @author jiangl
 *
 */
public class DockerBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private final KeyValueStorageProvider kvStorageProvider;
	private final FileStorageProvider fileStorageProvider;
	private final EventDispatcher eventDispatcher;
	private final ContainerSettings containerSettings = new ContainerSettings();
	private final ExecutionConfig executionConfig = new ExecutionConfig();
	private final TaskforceExecutionProperties executionProperties;
	private boolean stopped;
	private final Logger logger;
	private ThreadLocal<Map<String, Object>> localVariables = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};
	private final Map<String, BlockExecutionProgress> executionThreadInfo = new HashMap<>();

	public DockerBlockExecutionContext(String workspaceId, BlockExecutorFactory factory,
			KeyValueStorageProvider kvStorageProvider, FileStorageProvider fileStorageProvider,
			EventDispatcher eventDispatcher, TaskforceExecutionProperties execProperties) {
		this.blockExecutorFactory = factory;
		this.workspaceId = workspaceId;
		this.kvStorageProvider = kvStorageProvider;
		this.fileStorageProvider = fileStorageProvider;
		this.eventDispatcher = eventDispatcher;
		this.executionProperties = execProperties;

		this.logger = initializeLogger();

		File homeDir = new File(this.executionProperties.getHomeDir() + File.separator + workspaceId);
		if (!homeDir.exists()) {
			homeDir.mkdirs();
			logger.info("Created taskforce execution home dir {}", homeDir);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Logger initializeLogger() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern(this.executionProperties.getLogPattern());
		encoder.start();

		FileAppender appender = new FileAppender<>();
		appender.setContext(loggerContext);
		appender.setName(this.workspaceId);
		appender.setImmediateFlush(true);
		appender.setFile(this.executionProperties.getHomeDir() + "/" + this.workspaceId + "/logs/run.log");
		appender.setEncoder(encoder);
		appender.start();

		// attach the rolling file appender to the logger of your choice
		Logger logbackLogger = loggerContext.getLogger("Main");
		logbackLogger.setAdditive(false);
		logbackLogger.addAppender(appender);
		logbackLogger.info("Workspace execution logger initialized. workspaceId: {}", this.workspaceId);
		return logbackLogger;
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
		FileAppender<?> appender = (FileAppender<?>) this.logger.getAppender(workspaceId);
		appender.stop();
		appender.getEncoder().stop();
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
	public EventDispatcher getEventDispatcher() {
		return this.eventDispatcher;
	}

	public ContainerSettings getContainerSettings() {
		return containerSettings;
	}

	public ExecutionConfig getExecutionConfig() {
		return executionConfig;
	}

	@Override
	public boolean isStopped() {
		return this.stopped;
	}

	@Override
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	@Override
	public TaskforceExecutionProperties getExecutionProperties() {
		return this.executionProperties;
	}

	@Override
	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public void setLocalVariable(String id, Object value) {
		localVariables.get().put(id, value);
	}

	@Override
	public Object getLocalVariable(String id) {
		return localVariables.get().get(id);
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

	@Override
	public Map<String, BlockExecutionProgress> getBlockExecutionProgress() {
		return this.executionThreadInfo;
	}

}
