/**
 * 
 */
package org.teapotech.taskforce.execution;

/**
 * @author jiangl
 *
 */
public interface TaskExecutionContext {

	String getTaskforceId();

	void setTaskVariable(String key, Object value);

	Object getTaskVariable(String key);
}
