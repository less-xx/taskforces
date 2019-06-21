/**
 * 
 */
package org.teapotech.block.executor.event;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.teapotech.block.event.BlockEvent;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Block.Next;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.support.RabbitMQEventSupport;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
public class EventHandlerBlockExecutor extends AbstractBlockExecutor implements RabbitMQEventSupport {

	private RabbitAdmin rabbitAdmin;
	private TopicExchange eventExchange;

	public EventHandlerBlockExecutor(Block block) {
		super(block);
	}

	public EventHandlerBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

	@Override
	public void setEventExchange(TopicExchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		String workspaceId = context.getWorkspaceId();
		String blockType = this.block.getType();
		String blockId = this.block.getId();
		// TODO
		String queueName = "workspace." + workspaceId + "." + blockType + "." + blockId;
		Queue eventQueue = new Queue(queueName);
		rabbitAdmin.declareQueue(eventQueue);
		String routingKey = queueName;
		// TODO routingkey is not correct.
		Binding binding = BindingBuilder.bind(eventQueue).to(eventExchange).with(routingKey);
		rabbitAdmin.declareBinding(binding);
		LOG.info("Event binding: {}", binding);

		RabbitTemplate rabbitTemplate = rabbitAdmin.getRabbitTemplate();

		try {
			while (!context.isStopped()) {
				BlockEvent evt = (BlockEvent) rabbitTemplate.receiveAndConvert(queueName, 5000L);
				if (evt == null) {
					continue;
				}
				LOG.info("Received event: {}", evt);

				Next next = this.block.getNext();
				if (next != null && next.getBlock() != null) {
					BlockExecutorUtils.execute(next.getBlock(), context);
				}
			}
		} catch (AmqpException ie) {
			LOG.error(ie.getMessage());
			rabbitAdmin.deleteQueue(queueName);
		}
		return null;
	}

}
