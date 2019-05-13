/**
 * 
 */
package org.teapotech.taskforce.storage;

import org.teapotech.taskforce.entity.CustomResourcePath;
import org.teapotech.taskforce.entity.CustomResourcePath.AccessType;
import org.teapotech.taskforce.exception.InvalidTaskforceStorageConfigurationException;
import org.teapotech.taskforce.util.JSONUtils;

/**
 * @author lessdev
 *
 */
public class TaskforceStorageConfigFactory {

	public static TaskforceStorageConfig createConfig(CustomResourcePath storageDef)
			throws InvalidTaskforceStorageConfigurationException {
		String conf = storageDef.getConfiguration();

		try {
			if (storageDef.getStorageType() == AccessType.File) {
				return JSONUtils.getObject(conf, FileStorageConfig.class);
			}
		} catch (Exception e) {
			throw new InvalidTaskforceStorageConfigurationException(e.getMessage(), e);
		}

		throw new InvalidTaskforceStorageConfigurationException("Unknown storage type " + storageDef.getStorageType());
	}

}
