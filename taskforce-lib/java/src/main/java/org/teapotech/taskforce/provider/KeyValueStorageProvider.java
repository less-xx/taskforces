/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;

/**
 * @author jiangl
 *
 */
public interface KeyValueStorageProvider {

	Object get(String taskforceId, String key);

	void put(String taskforceId, String key, Object value);

	void remove(String taskforceId, String key);

	Collection<String> getAllKeys(String taskforceId);

	void destroy(String taskforceId);
}
