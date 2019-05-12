/**
 * 
 */
package org.teapotech.taskforce.storage;

import org.teapotech.taskforce.entity.CustomStorageConfigEntity;
import org.teapotech.taskforce.entity.CustomStorageConfigEntity.StorageType;
import org.teapotech.taskforce.exception.InvalidTaskforceStorageConfigurationException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lessdev
 *
 */
public class TaskforceStorageConfigFactory {

	private static ObjectMapper mapper = new ObjectMapper();

	public static TaskforceStorageConfig createConfig(CustomStorageConfigEntity storageDef)
			throws InvalidTaskforceStorageConfigurationException {
		String conf = storageDef.getConfiguration();

		try {
			if (storageDef.getStorageType() == StorageType.File) {
				return mapper.readValue(conf, FileStorageConfig.class);
			}
		} catch (Exception e) {
			throw new InvalidTaskforceStorageConfigurationException(e.getMessage(), e);
		}

		throw new InvalidTaskforceStorageConfigurationException("Unknown storage type " + storageDef.getStorageType());
	}

}
