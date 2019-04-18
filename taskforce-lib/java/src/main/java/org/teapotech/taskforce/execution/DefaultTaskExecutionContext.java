/**
 * 
 */
package org.teapotech.taskforce.execution;

import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author jiangl
 *
 */
public class DefaultTaskExecutionContext implements TaskExecutionContext {

	private final String taskforceId;
	private final TaskforceStorageProvider storageProvider;

	DefaultTaskExecutionContext(String taskforceId,
			TaskforceStorageProvider storageProvider) {
		this.taskforceId = taskforceId;
		this.storageProvider = storageProvider;
		this.storageProvider.setTaskforceId(taskforceId);
	}

	@Override
	public String getTaskforceId() {
		return this.taskforceId;
	}

	@Override
	public void setTaskVariable(String key, Object value) {
		storageProvider.put(key, value);
	}

	@Override
	public Object getTaskVariable(String key) {
		return storageProvider.get(key);
	}

}
