/**
 * 
 */
package org.teapotech.block.executor;

import java.io.InputStream;
import java.util.Collection;

import org.teapotech.block.BlockExecutorFactory;
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

	public DefaultBlockExecutionContext(String workspaceId, BlockExecutorFactory blockExecutorFactory) {
		this.blockExecutorFactory = blockExecutorFactory;
		this.workspaceId = workspaceId;
	}

	public DefaultBlockExecutionContext(String workspaceId) {
		this.blockExecutorFactory = BlockExecutorFactory.build();
		this.workspaceId = workspaceId;
	}

	public void setKeyValueStorageProvider(KeyValueStorageProvider storageProvider) {
		this.kvStorageProvider = storageProvider;
	}

	public void setFileStorageProvider(FileStorageProvider fileStorageProvider) {
		this.fileStorageProvider = fileStorageProvider;
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
}
