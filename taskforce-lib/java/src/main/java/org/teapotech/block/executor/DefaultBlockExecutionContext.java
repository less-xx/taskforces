/**
 * 
 */
package org.teapotech.block.executor;

import java.util.Collection;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
public class DefaultBlockExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String workspaceId;
	private TaskforceStorageProvider storageProvider;

	public DefaultBlockExecutionContext(String workspaceId, BlockExecutorFactory blockExecutorFactory) {
		this.blockExecutorFactory = blockExecutorFactory;
		this.workspaceId = workspaceId;
	}

	public DefaultBlockExecutionContext(String workspaceId) {
		this.blockExecutorFactory = BlockExecutorFactory.build();
		this.workspaceId = workspaceId;
	}

	public void setStorageProvider(TaskforceStorageProvider storageProvider) {
		this.storageProvider = storageProvider;
	}

	@Override
	public BlockExecutorFactory getBlockExecutorFactory() {
		return this.blockExecutorFactory;
	}

	@Override
	public Object getVariable(String id) {
		return storageProvider.get(workspaceId, id);
	}

	@Override
	public void setVariable(String id, Object value) {
		storageProvider.put(workspaceId, id, value);
	}

	@Override
	public Collection<String> getAllVariableNames() {
		return storageProvider.getAllKeys(workspaceId);
	}

	@Override
	public String getWorkspaceId() {
		return workspaceId;
	}

	@Override
	public void destroy() {
		storageProvider.destroy(workspaceId);
	}
}
