package org.teapotech.taskforce.task.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.event.BlockEventRabbitMQDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventRabbitMQDispatcher;

@Configuration
public class WorkspaceEventRabbitMQAutoConfig {

	private static Logger LOG = LoggerFactory.getLogger(WorkspaceEventRabbitMQAutoConfig.class);

	@Value("${taskforce.event.rabbitmq.exchange}")
	String taskforceEventExchangeName;

	@Autowired
	private ConnectionFactory rabbitConnectionFactory;

	@PostConstruct
	void init() {
		TopicExchange taskforceEventExchange = taskforceEventExchange();
		RabbitAdmin rabbitAdmin = rabbitAdmin(rabbitConnectionFactory);
		rabbitAdmin.declareExchange(taskforceEventExchange);
		LOG.info("Declared block event exchange {}", taskforceEventExchange);

		Binding binding = BindingBuilder.bind(workspaceExecutionEventQueue()).to(taskforceEventExchange)
				.with("workspace.execution.*");
		rabbitAdmin.declareBinding(binding);
	}

	@Bean
	RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
		return new RabbitAdmin(cf);
	}

	@Bean("taskforce-event-exchange")
	TopicExchange taskforceEventExchange() {
		return (TopicExchange) ExchangeBuilder
				.topicExchange(taskforceEventExchangeName)
				.durable(true)
				.build();
	}

	@Bean("block-event-rabbit-template")
	RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter converter) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter);
		rabbitTemplate.setExchange(taskforceEventExchangeName);
		return rabbitTemplate;
	}

	@Bean
	Jackson2JsonMessageConverter messageConverter() throws Exception {

		return new Jackson2JsonMessageConverter();
	}

	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			final ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter);
		return factory;
	}

	@Bean
	BlockEventDispatcher blockEventDispatcher(final ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter converter) {
		return new BlockEventRabbitMQDispatcher(rabbitTemplate(connectionFactory, converter));
	}

	@Bean
	WorkspaceEventDispatcher workspaceEventDispatcher(final ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter converter) {
		return new WorkspaceEventRabbitMQDispatcher(rabbitTemplate(connectionFactory, converter));
	}

	@Bean
	Queue workspaceExecutionEventQueue() {
		return new Queue(WorkspaceEventDispatcher.QUEUE_WORKSPACE_EXECUTION_EVENT);
	}

}
