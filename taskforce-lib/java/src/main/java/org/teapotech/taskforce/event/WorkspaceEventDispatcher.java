/**
 * 
 */
package org.teapotech.taskforce.event;

import org.teapotech.block.event.WorkspaceExecutionEvent;

/**
 * @author jiangl
 *
 */
public interface WorkspaceEventDispatcher {

	public final static String QUEUE_WORKSPACE_EXECUTION_EVENT = "workspace.execution.event.queue";

	void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event);

}
