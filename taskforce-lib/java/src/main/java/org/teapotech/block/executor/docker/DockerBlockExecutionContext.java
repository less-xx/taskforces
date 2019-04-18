/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Collection;

import org.apache.commons.lang3.RandomStringUtils;
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
	private final String taskforceId = RandomStringUtils.randomAlphanumeric(13).toLowerCase();
	private TaskforceStorageProvider storageProvider;

	public DockerBlockExecutionContext(BlockExecutorFactory factory) {
		this.blockExecutorFactory = factory;
	}

	public String getTaskforceId() {
		return this.taskforceId;
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
}
