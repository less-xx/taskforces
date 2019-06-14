/**
 * 
 */
package org.teapotech.block.executor;

import java.io.InputStream;
import java.util.Collection;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageException;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

/**
 * @author jiangl
 *
 */
public class DefaultBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private KeyValueStorageProvider kvStorageProvider;
	private FileStorageProvider fileStorageProvider;
	private BlockEventDispatcher blockEventDispatcher;

	public DefaultBlockExecutionContext(String workspaceId, BlockExecutorFactory blockExecutorFactory) {
		this.blockExecutorFactory = blockExecutorFactory;
		this.workspaceId = workspaceId;
	}

	public DefaultBlockExecutionContext(String workspaceId) {
		BlockRegistryManager brm = new BlockRegistryManager();
		brm.loadBlockRegistries();
		this.blockExecutorFactory = BlockExecutorFactory.build(brm);
		this.workspaceId = workspaceId;
	}

	public void setKeyValueStorageProvider(KeyValueStorageProvider storageProvider) {
		this.kvStorageProvider = storageProvider;
	}

	public void setFileStorageProvider(FileStorageProvider fileStorageProvider) {
		this.fileStorageProvider = fileStorageProvider;
	}

	public void setBlockEventDispatcher(BlockEventDispatcher blockEventDispatcher) {
		this.blockEventDispatcher = blockEventDispatcher;
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
	public BlockEventDispatcher getBlockEventDispatcher() {
		return this.blockEventDispatcher;
	}
}
