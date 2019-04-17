/**
 * 
 */
package org.teapotech.taskforce.context;

import org.apache.commons.lang3.RandomStringUtils;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Variable;
import org.teapotech.taskforce.exception.TaskforceStorageException;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
public class DockerTaskExecutionContext implements BlockExecutionContext {

	private final BlockExecutorFactory blockExecutorFactory;
	private final String taskforceId = RandomStringUtils.randomAlphanumeric(13).toLowerCase();
	private TaskforceStorageProvider storageProvider;

	public DockerTaskExecutionContext(BlockExecutorFactory factory) {
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
	public Variable getVariable(String id) {
		if (storageProvider == null) {
			throw new TaskforceStorageException("TaskforceStorageProvider is not configured.");
		}
		return storageProvider.get(key);
	}

	@Override
	public void setVariable(String id, Variable var) {
		// TODO Auto-generated method stub

	}
}
