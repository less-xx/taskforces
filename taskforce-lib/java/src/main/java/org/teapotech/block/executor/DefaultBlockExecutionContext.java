/**
 * 
 */
package org.teapotech.block.executor;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.provider.FileStorageException;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.task.config.TaskforceExecutionProperties;

/**
 * @author jiangl
 *
 */
public class DefaultBlockExecutionContext implements BlockExecutionContext {

	private static Logger LOG = LoggerFactory.getLogger(DefaultBlockExecutionContext.class);

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private KeyValueStorageProvider kvStorageProvider;
	private FileStorageProvider fileStorageProvider;
	private EventDispatcher eventDispatcher;
	private boolean stopped;
	private final TaskforceExecutionProperties executionProperties;
	private ThreadLocal<Map<String, Object>> localVariables = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};

	public DefaultBlockExecutionContext(String workspaceId, BlockExecutorFactory blockExecutorFactory,
			TaskforceExecutionProperties executionProperties) {
		this.blockExecutorFactory = blockExecutorFactory;
		this.workspaceId = workspaceId;
		this.executionProperties = executionProperties;
	}

	public DefaultBlockExecutionContext(String workspaceId, TaskforceExecutionProperties executionProperties) {
		BlockRegistryManager brm = new BlockRegistryManager();
		brm.loadBlockRegistries();
		this.blockExecutorFactory = BlockExecutorFactory.build(brm);
		this.workspaceId = workspaceId;
		this.executionProperties = executionProperties;
	}

	public void setKeyValueStorageProvider(KeyValueStorageProvider storageProvider) {
		this.kvStorageProvider = storageProvider;
	}

	public void setFileStorageProvider(FileStorageProvider fileStorageProvider) {
		this.fileStorageProvider = fileStorageProvider;
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public Object getVariable(String id) {
		return kvStorageProvider.get(workspaceId, id);
	}

	@Override
	public void setVariable(String id, Object value) {
		kvStorageProvider.put(workspaceId, id, value);
	}

	@Override
	public Collection<String> getAllVariableNames() {
		return kvStorageProvider.getAllKeys(workspaceId);
	}

	@Override
	public String getWorkspaceId() {
		return workspaceId;
	}

	@Override
	public void destroy() {
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
	public boolean isStopped() {
		return this.stopped;
	}

	@Override
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	@Override
	public EventDispatcher getEventDispatcher() {
		return this.eventDispatcher;
	}

	@Override
	public TaskforceExecutionProperties getExecutionProperties() {
		return this.executionProperties;
	}

	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Override
	public void setLocalVariable(String id, Object value) {
		localVariables.get().put(id, value);

	}

	@Override
	public Object getLocalVariable(String id) {
		return localVariables.get().get(id);
	}

}
