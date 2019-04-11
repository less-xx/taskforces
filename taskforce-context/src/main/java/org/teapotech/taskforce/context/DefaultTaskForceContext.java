/**
 * 
 */
package org.teapotech.taskforce.context;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.exception.TaskforceStorageException;
import org.teapotech.taskforce.provider.TaskforceStorageProvider;

/**
 * @author lessdev
 *
 */
public class DefaultTaskForceContext implements TaskforceContext {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultTaskForceContext.class);

	private final String taskforceId;

	private TaskforceStorageProvider storageProvider;

	public DefaultTaskForceContext(String taskforceId) {
		if (StringUtils.isBlank(taskforceId)) {
			this.taskforceId = RandomStringUtils.randomAlphanumeric(13).toLowerCase();
		} else {
			this.taskforceId = taskforceId;
		}

	}

	public TaskforceStorageProvider getTaskforceResultStorageProvider() {
		return storageProvider;
	}

	public void setTaskforceResultStorageProvider(TaskforceStorageProvider storageProvider) {
		this.storageProvider = storageProvider;
	}

	@Override
	public String getTaskforceId() {
		return this.taskforceId;
	}

	@Override
	public void store(String key, Object value) {
		if (storageProvider == null) {
			throw new TaskforceStorageException("TaskforceStorageProvider is not configured.");
		}
		storageProvider.put(key, value);
	}

	@Override
	public Object load(String key) {
		if (storageProvider == null) {
			throw new TaskforceStorageException("TaskforceStorageProvider is not configured.");
		}
		return storageProvider.get(key);
	}
}
