/**
 * 
 */
package org.teapotech.taskforce.context;

/**
 * @author lessdev
 *
 */
public interface TaskforceContext {

	String getTaskforceId();

	void store(String key, Object value);

	Object load(String key);

}
