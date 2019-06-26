/**
 * 
 */
package org.teapotech.taskforce.event;

import org.teapotech.block.event.BlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;

/**
 * @author jiangl
 *
 */
public interface EventDispatcher {

	void dispatchBlockEvent(BlockEvent event);

	void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event);
}
