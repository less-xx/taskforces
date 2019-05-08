/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.io.InputStream;

/**
 * @author jiangl
 *
 */
public interface FileStorageProvider {

	void store(String taskforceId, String key, InputStream in) throws FileStorageException;

	InputStream load(String taskforceId, String key) throws FileStorageException;

	void delete(String taskforceId, String key) throws FileStorageException;

}
