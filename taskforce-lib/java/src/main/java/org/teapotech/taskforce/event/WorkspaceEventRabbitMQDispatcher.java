/**
 * 
 */
package org.teapotech.taskforce.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.teapotech.block.event.WorkspaceExecutionEvent;

/**
 * @author jiangl
 *
 */
public class WorkspaceEventRabbitMQDispatcher implements WorkspaceEventDispatcher {

	private static Logger LOG = LoggerFactory.getLogger(WorkspaceEventRabbitMQDispatcher.class);

	private final RabbitTemplate eventDispatcher;

	public WorkspaceEventRabbitMQDispatcher(RabbitTemplate dispatcher) {
		this.eventDispatcher = dispatcher;
	}

	@Override
	public void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event) {
		try {
			String routingKey = "workspace.execution." + event.getWorkspaceId();
			eventDispatcher.convertAndSend(routingKey, event);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
