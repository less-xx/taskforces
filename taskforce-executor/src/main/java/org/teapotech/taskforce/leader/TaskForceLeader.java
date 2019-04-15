/**
 * 
 */
package org.teapotech.taskforce.leader;

import java.util.Collection;

import org.teapotech.taskforce.context.TaskforceContext;
import org.teapotech.taskforce.dto.TaskDescriptor;
import org.teapotech.taskforce.exception.TaskDescriptorException;
import org.teapotech.taskforce.exception.TaskExecutionException;

/**
 * @author jiangl
 *
 */
public interface TaskForceLeader {

	TaskforceContext getContext();

	Collection<TaskDescriptor> getAllTaskDescriptors() throws TaskDescriptorException;

	TaskDescriptor getTaskDescriptorByName(String name) throws TaskDescriptorException;

	void execute(TaskDescriptor taskDescriptor) throws TaskExecutionException;

}
