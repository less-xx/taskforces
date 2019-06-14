/**
 * 
 */
package org.teapotech.taskforce.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.teapotech.block.event.BlockEvent;

/**
 * @author jiangl
 *
 */
public class BlockEventRabbitMQDispatcher implements BlockEventDispatcher {

	private static Logger LOG = LoggerFactory.getLogger(BlockEventRabbitMQDispatcher.class);

	private final RabbitTemplate eventDispatcher;

	public BlockEventRabbitMQDispatcher(RabbitTemplate dispatcher) {
		this.eventDispatcher = dispatcher;
	}

	@Override
	public void dispatchEvent(BlockEvent event) {
		try {
			String routingKey = "workspace." + event.getWorkspaceId() + ".#";
			eventDispatcher.convertAndSend(routingKey, event);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
