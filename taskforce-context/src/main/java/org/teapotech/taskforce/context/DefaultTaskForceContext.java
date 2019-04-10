/**
 * 
 */
package org.teapotech.taskforce.context;

import org.apache.commons.lang3.RandomStringUtils;
import org.teapotech.taskforce.provider.TaskforceResultStorageProvider;

/**
 * @author lessdev
 *
 */
public class DefaultTaskForceContext implements TaskforceContext {

	private final String taskforceId;
	private TaskforceResultStorageProvider taskforceResultStorageProvider;

	public DefaultTaskForceContext(String taskforceId) {
		if (taskforceId == null) {
			this.taskforceId = RandomStringUtils.randomAlphanumeric(13);
		} else {
			this.taskforceId = taskforceId;
		}
	}

	public TaskforceResultStorageProvider getTaskforceResultStorageProvider() {
		return taskforceResultStorageProvider;
	}

	public void setTaskforceResultStorageProvider(TaskforceResultStorageProvider taskforceResultStorageProvider) {
		this.taskforceResultStorageProvider = taskforceResultStorageProvider;
	}

	@Override
	public String getTaskforceId() {
		return this.taskforceId;
	}
}
