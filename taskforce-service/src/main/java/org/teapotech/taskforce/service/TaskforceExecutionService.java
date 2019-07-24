/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.event.BlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.exception.InvalidWorkspaceException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.BlockXmlUtils;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.event.RabbitMQEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.repo.TaskforceExecutionQuerySpecs;
import org.teapotech.taskforce.repo.TaskforceExecutionRepo;

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
	EventDispatcher blockEvtDispatcher;

	@Autowired
	TaskforceExecutionRepo tfExecRepo;

	private final ConcurrentHashMap<String, WorkspaceExecutor> workspaceExecutors = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		// String queueName = "queue.workspace.events";
		// Queue eventQueue = new Queue(queueName);
		// rabbitAdmin.declareQueue(eventQueue);
		// String routingKey = "workspace.#";
		// Binding binding =
		// BindingBuilder.bind(eventQueue).to(eventExchange).with(routingKey);
		// rabbitAdmin.declareBinding(binding);
		// LOG.info("Event binding: {}", binding);
		// SimpleMessageListenerContainer container = new
		// SimpleMessageListenerContainer();
		// container.setConnectionFactory(rabbitConnectionFactory);
		// container.setQueues(eventQueue);
		// MessageListenerAdapter adapter = new MessageListenerAdapter(this,
		// "handleEvent");
		// adapter.setMessageConverter(new Jackson2JsonMessageConverter());
		// container.setMessageListener(adapter);
		// container.start();
	}

	public Workspace getWorkspace(TaskforceEntity taskforceEntity) throws InvalidWorkspaceException {
		try {
			return BlockXmlUtils.loadWorkspace(taskforceEntity.getConfiguration());
		} catch (Exception e) {
			throw new InvalidWorkspaceException(e.getMessage(), e);
		}
	}

	public TaskforceExecution executeWorkspace(TaskforceEntity taskforce) throws InvalidWorkspaceException {

		TaskforceExecution tfExec = new TaskforceExecution();
		tfExec.setCreatedTime(new Date());
		tfExec.setTaskforce(taskforce.toSimple());
		tfExec = saveTaskforceExecution(tfExec);

		Workspace w = getWorkspace(taskforce);
		BlockExecutionContext context = createWorkspaceExecutionContext(tfExec.getId());

		executeWorkspace(w, context);
		return tfExec;
	}

	public void stopTaskfroceExecution(TaskforceExecution taskExec) {
		WorkspaceExecutor wExecutor = workspaceExecutors.get(taskExec.getId());
		if (wExecutor != null) {
			wExecutor.stop();
		}
	}

	public BlockExecutionContext createWorkspaceExecutionContext(String taskforceId) {
		return new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider, fileStorageProvider,
				blockEvtDispatcher);
	}

	public void executeWorkspace(Workspace workspace, BlockExecutionContext context) {

		WorkspaceExecutor wExecutor = new WorkspaceExecutor(workspace, context);
		workspaceExecutors.put(context.getWorkspaceId(), wExecutor);
		try {
			wExecutor.execute();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Transactional
	private TaskforceExecution saveTaskforceExecution(TaskforceExecution tfExec) {
		return tfExecRepo.save(tfExec);
	}

	@Transactional
	private int updateTaskforceExecutionStatus(String taskforceId, Status status) {
		return tfExecRepo.updateTaskforceExecution(taskforceId, status);
	}

	@Transactional
	public TaskforceExecution getTaskforceExecutionById(String id) {
		return tfExecRepo.findById(id).orElse(null);
	}

	@Transactional
	public TaskforceExecution getAliveTaskforceExecutionByTaskforce(TaskforceEntity taskforce) {
		return tfExecRepo.findOneByTaskforceAndStatusIn(taskforce.toSimple(),
				Arrays.asList(Status.Waiting, Status.Running, Status.Stopping));
	}

	@Transactional
	public Page<TaskforceExecution> query(String id, String taskforceId, Collection<Status> status,
			Date createdTime, String createdBy, Pageable pageable) {
		return tfExecRepo.findAll(
				TaskforceExecutionQuerySpecs.queryTaskforceExecution(id, taskforceId, status, createdTime, createdBy),
				pageable);
	}

	public void handleEvent(BlockEvent event) {
		LOG.info("Received event: {}", event);
	}

	@RabbitListener(queues = RabbitMQEventDispatcher.QUEUE_WORKSPACE_EXECUTION_EVENT)
	public void handleWorkspaceExecutionEvent(final WorkspaceExecutionEvent event) {
		LOG.info("Workspace event. ID: {}, Status: {}", event.getWorkspaceId(), event.getStatus());

		try {
			updateTaskforceExecutionStatus(event.getWorkspaceId(), event.getStatus());

			if (event.getStatus() == Status.Stopped) {
				workspaceExecutors.remove(event.getWorkspaceId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
