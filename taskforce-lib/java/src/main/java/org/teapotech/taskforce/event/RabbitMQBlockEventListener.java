package org.teapotech.taskforce.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.teapotech.block.event.NamedBlockEvent;

public class RabbitMQBlockEventListener implements BlockEventListener {

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	private Binding binding;
	private final RabbitAdmin rabbitAdmin;
	private final TopicExchange eventExchange;
	private String id;
	private String routingKey;
	private final static MessageConverter messageConverter = new Jackson2JsonMessageConverter();
	private final RabbitTemplate rabbitTemplate;

	public RabbitMQBlockEventListener(RabbitAdmin rabbitAdmin, TopicExchange eventExchange) {
		this.rabbitAdmin = rabbitAdmin;
		this.eventExchange = eventExchange;
		this.rabbitTemplate = rabbitAdmin.getRabbitTemplate();
		this.rabbitTemplate.setMessageConverter(messageConverter);
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getRoutingKey() {
		return this.routingKey;
	}

	@Override
	public NamedBlockEvent receive(int timeoutSeconds) throws InterruptedException {
		NamedBlockEvent evt = rabbitTemplate.receiveAndConvert(this.id, timeoutSeconds * 1000L,
				new ParameterizedTypeReference<NamedBlockEvent>() {
				});
		return evt;
	}

	@Override
	public void initialize(String routingKey) {
		this.routingKey = routingKey;
		Queue eventQueue = new Queue(this.id);
		rabbitAdmin.declareQueue(eventQueue);
		this.binding = BindingBuilder.bind(eventQueue).to(eventExchange).with(routingKey);
		rabbitAdmin.declareBinding(binding);
		LOG.info("Event binding: {}", binding);
	}

	@Override
	public void destroy() {
		if (this.binding != null) {
			rabbitAdmin.removeBinding(binding);
		}
		rabbitAdmin.deleteQueue(this.id);

	}

}
