/**
 * 
 */
package org.teapotech.taskforce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.block.model.Workspace;

/**
 * @author jiangl
 *
 */
@Service
public class TaskforceExecutionService {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceExecutionService.class);

	@Autowired
	RabbitAdmin rabbitAdmin;

	@Autowired
	TopicExchange eventExchange;

	@Autowired
	ConnectionFactory rabbitConnectionFactory;

	private void executeWorkspace(Workspace workspace) {
		String queueName = "queue.workspace." + workspace.getId();
		Queue eventQueue = new Queue(queueName);
		String routingKey = "workspace." + workspace.getId() + ".#";
		Binding binding = BindingBuilder.bind(eventQueue).to(eventExchange).with(routingKey);
		rabbitAdmin.declareBinding(binding);
		LOG.info("Event binding: {}", binding);

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory);
		container.setQueues(eventQueue);
		MessageListenerAdapter listener = new MessageListenerAdapter() {
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				super.onMessage(message);
			}
		};
		listener.setMessageConverter(new Jackson2JsonMessageConverter());
		container.setMessageListener(listener);

		// TODO run

	}
}
