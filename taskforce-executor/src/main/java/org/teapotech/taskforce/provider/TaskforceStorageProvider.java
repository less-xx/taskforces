/**
 * 
 */
package org.teapotech.taskforce.provider;

/**
 * @author jiangl
 *
 */
public interface TaskforceStorageProvider {

	void setTaskforceId(String taskforceId);

	Object get(String key);

	void put(String key, Object value);
}
