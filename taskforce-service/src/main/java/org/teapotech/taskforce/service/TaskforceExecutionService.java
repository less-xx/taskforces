/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.event.BlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent.Status;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

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

	@Autowired
	KeyValueStorageProvider kvStorageProvider;

	@Autowired
	FileStorageProvider fileStorageProvider;

	@Autowired
	BlockExecutorFactory factory;

	@Autowired
	BlockEventDispatcher blockEvtDispatcher;

	@Autowired
	WorkspaceEventDispatcher workspaceEventDispatcher;

	private final ConcurrentHashMap<String, WorkspaceExecutor> workspaceExecutors = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		String queueName = "queue.workspace.events";
		Queue eventQueue = new Queue(queueName);
		rabbitAdmin.declareQueue(eventQueue);
		String routingKey = "workspace.#";
		Binding binding = BindingBuilder.bind(eventQueue).to(eventExchange).with(routingKey);
		rabbitAdmin.declareBinding(binding);
		LOG.info("Event binding: {}", binding);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory);
		container.setQueues(eventQueue);
		MessageListenerAdapter adapter = new MessageListenerAdapter(this, "handleEvent");
		adapter.setMessageConverter(new Jackson2JsonMessageConverter());
		container.setMessageListener(adapter);
		container.start();
	}

	public BlockExecutionContext executeWorkspace(Workspace workspace) {

		String taskforceId = workspace.getId();
		DockerBlockExecutionContext context = new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider,
				fileStorageProvider, blockEvtDispatcher, workspaceEventDispatcher);
		WorkspaceExecutor wExecutor = new WorkspaceExecutor(workspace, context);
		workspaceExecutors.put(workspace.getId(), wExecutor);
		try {
			wExecutor.execute();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return context;
	}

	public void handleEvent(BlockEvent event) {
		LOG.info("Received event: {}", event);
	}

	@RabbitListener(queues = WorkspaceEventDispatcher.QUEUE_WORKSPACE_EXECUTION_EVENT)
	public void handleWorkspaceExecutionEvent(final WorkspaceExecutionEvent event) {
		LOG.info("Workspace event. ID: {}, Status: {}", event.getWorkspaceId(), event.getStatus());
		if (event.getStatus() == Status.Stopped) {
			workspaceExecutors.remove(event.getWorkspaceId());
		}
	}

}
