/**
 * 
 */
package org.teapotech.taskforce.event;

import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;

/**
 * @author jiangl
 *
 */
public interface EventDispatcher {

	void dispatchBlockEvent(NamedBlockEvent event);

	void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event);
}
