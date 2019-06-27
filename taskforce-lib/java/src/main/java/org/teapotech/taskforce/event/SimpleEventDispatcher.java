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
public class SimpleEventDispatcher implements EventDispatcher {

	private final SimpleEventExchange eventExchange;

	public SimpleEventDispatcher(SimpleEventExchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	@Override
	public void dispatchBlockEvent(NamedBlockEvent event) {
		String routingKey = "workspace." + event.getWorkspaceId() + "." + event.getEventName();
		eventExchange.dispatch(routingKey, event);
	}

	@Override
	public void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event) {
		// TODO Auto-generated method stub

	}

}
