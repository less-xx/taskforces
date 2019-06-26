/**
 * 
 */
package org.teapotech.taskforce.event;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.teapotech.block.model.Block;

/**
 * @author jiangl
 *
 */
public class RabbitMQBlockEventListenerFactory implements BlockEventListenerFactory {

	private final RabbitAdmin rabbitAdmin;
	private final TopicExchange eventExchange;

	public RabbitMQBlockEventListenerFactory(RabbitAdmin rabbitAdmin, TopicExchange eventExchange) {
		this.rabbitAdmin = rabbitAdmin;
		this.eventExchange = eventExchange;
	}

	@Override
	public BlockEventListener createBlockEventListener(String workspaceId, Block block) {
		RabbitMQBlockEventListener listener = new RabbitMQBlockEventListener(rabbitAdmin, eventExchange);
		String id = "workspace." + workspaceId + ".queue." + block.getId();
		listener.setId(id);

		String routingKey = "workspace." + workspaceId + ".block." + block.getId();
		listener.setRoutingKey(routingKey);
		// TODO get routing key from block field
		return listener;
	}

}
