/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;

/**
 * @author jiangl
 *
 */
public interface TaskforceStorageProvider {

	void setTaskforceId(String taskforceId);

	Object get(String key);

	void put(String key, Object value);

	Collection<String> getAllKeys();
}
