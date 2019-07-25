/**
 * 
 */
package org.teapotech.taskforce.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;

/**
 * @author jiangl
 *
 */
public class RabbitMQEventDispatcher implements EventDispatcher {

	public final static String QUEUE_WORKSPACE_EXECUTION_EVENT = "workspace.execution.event.queue";

	private static Logger LOG = LoggerFactory.getLogger(RabbitMQEventDispatcher.class);

	private final RabbitTemplate eventDispatcher;

	public RabbitMQEventDispatcher(RabbitTemplate dispatcher) {
		this.eventDispatcher = dispatcher;
	}

	@Override
	public void dispatchBlockEvent(NamedBlockEvent event) {
		try {
			String evtName = event.getEventName().replace("\\s*", "_");
			String routingKey = "workspace." + event.getWorkspaceId() + "." + evtName;
			eventDispatcher.convertAndSend(routingKey, event);
			LOG.info("Dispatched named block event, routing key: {}", routingKey);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	@Override
	public void dispatchWorkspaceExecutionEvent(WorkspaceExecutionEvent event) {
		try {
			String routingKey = "workspace.execution." + event.getWorkspaceId();
			eventDispatcher.convertAndSend(routingKey, event);
			LOG.info("Dispatched workspace execution event, routing key: {}", routingKey);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
