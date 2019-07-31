/**
 * 
 */
package org.teapotech.taskforce.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.event.BlockEvent;
import org.teapotech.block.event.WorkspaceExecutionEvent;
import org.teapotech.block.exception.InvalidWorkspaceException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.BlockXmlUtils;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.event.RabbitMQEventDispatcher;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.repo.TaskforceExecutionQuerySpecs;
import org.teapotech.taskforce.repo.TaskforceExecutionRepo;
import org.teapotech.taskforce.task.config.TaskforceExecutionProperties;

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

	@Autowired
	TaskforceDataStoreService taskforceDataStoreService;

	@Autowired
	TaskforceExecutionProperties executionProperties;

	private final ConcurrentHashMap<String, WorkspaceExecutor> workspaceExecutors = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		List<TaskforceExecution> activeExecs = getActiveTaskforceExecutions();
		for (TaskforceExecution te : activeExecs) {
			try {
				Workspace w = getWorkspace(te.getTaskforce());
				String workspaceId = buildWorkspaceId(te);
				BlockExecutionContext context = createWorkspaceExecutionContext(workspaceId);

				WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
				workspaceExecutors.put(context.getWorkspaceId(), wExecutor);
				wExecutor.startMonitoring();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private String buildWorkspaceId(String taskforceId, Long taskExecId) {
		return taskforceId + "_" + taskExecId;
	}

	private String buildWorkspaceId(TaskforceExecution exec) {
		return buildWorkspaceId(exec.getTaskforce().getId(), exec.getId());
	}

	public Workspace getWorkspace(TaskforceEntity taskforceEntity) throws InvalidWorkspaceException {
		try {
			return BlockXmlUtils.loadWorkspace(taskforceEntity.getConfiguration());
		} catch (Exception e) {
			throw new InvalidWorkspaceException(e.getMessage(), e);
		}
	}

	public Workspace getWorkspace(SimpleTaskforceEntity taskforceEntity) throws InvalidWorkspaceException {
		TaskforceEntity te = taskforceDataStoreService.findTaskforceEntityById(taskforceEntity.getId());
		return getWorkspace(te);
	}

	public TaskforceExecution executeWorkspace(TaskforceEntity taskforce) throws InvalidWorkspaceException {

		TaskforceExecution tfExec = new TaskforceExecution();
		tfExec.setCreatedTime(new Date());
		tfExec.setTaskforce(taskforce.toSimple());
		tfExec = saveTaskforceExecution(tfExec);

		Workspace w = getWorkspace(taskforce);
		String workspaceId = buildWorkspaceId(tfExec);
		BlockExecutionContext context = createWorkspaceExecutionContext(workspaceId);

		executeWorkspace(w, context);
		return tfExec;
	}

	public void stopTaskfroceExecution(TaskforceExecution taskExec) {
		String workspaceId = buildWorkspaceId(taskExec);
		WorkspaceExecutor wExecutor = workspaceExecutors.get(workspaceId);
		if (wExecutor != null) {
			wExecutor.stop();
		}
	}

	public BlockExecutionContext createWorkspaceExecutionContext(String taskforceId) {
		return new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider, fileStorageProvider,
				blockEvtDispatcher, executionProperties);
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
	private int updateTaskforceExecutionStatus(Long taskforceExecId, Status status) {
		return tfExecRepo.updateTaskforceExecution(taskforceExecId, status);
	}

	@Transactional
	public TaskforceExecution getTaskforceExecutionById(Long id) {
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

	public Map<String, BlockExecutionProgress> getBlockExecutionProgress(TaskforceExecution taskExec) {
		String workspaceId = buildWorkspaceId(taskExec);
		WorkspaceExecutor we = workspaceExecutors.get(workspaceId);
		if (we != null) {
			return we.getBlockExecutionContext().getBlockExecutionProgress();
		}
		return null;
	}

	@Transactional
	private List<TaskforceExecution> getActiveTaskforceExecutions() {
		Page<TaskforceExecution> result = tfExecRepo.findAll(
				TaskforceExecutionQuerySpecs.queryTaskforceExecution(null, null,
						Arrays.asList(Status.Waiting, Status.Running, Status.Stopping), null, null),
				PageRequest.of(0, 1000));
		return result.getContent();
	}

	public File getTaskforceExecutionLogFile(TaskforceExecution exec) {
		String workspaceId = buildWorkspaceId(exec);
		File logFile = new File(
				executionProperties.getHomeDir() + File.separator + workspaceId + File.separator + "logs/run.log");
		return logFile;
	}

	public String getLogFileContent(TaskforceExecution exec, int start, int lineNum) throws IOException {
		StringBuilder sb = new StringBuilder();
		File logFile = getTaskforceExecutionLogFile(exec);
		if (!logFile.exists()) {
			LOG.error("Cannot find log file at {}", logFile.getAbsolutePath());
			return sb.toString();
		}
		try (FileInputStream fis = new FileInputStream(logFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));) {
			int l = 0;
			String line = null;
			while (l++ < start) {
				line = br.readLine();
				if (line == null) {
					return sb.toString();
				}
			}
			while (l++ < start + lineNum) {
				line = br.readLine();
				if (line != null) {
					sb.append(line).append("\n");
				} else {
					return sb.toString();
				}
			}
		}

		return sb.toString();
	}

	public void readLogFileContent(TaskforceExecution exec, int start, int lineNum, OutputStream out)
			throws IOException {
		File logFile = getTaskforceExecutionLogFile(exec);
		if (!logFile.exists()) {
			LOG.error("Cannot find log file at {}", logFile.getAbsolutePath());
			return;
		}

		try (FileInputStream fis = new FileInputStream(logFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				PrintWriter pw = new PrintWriter(out);) {
			int l = 0;
			String line = null;
			while (l++ < start) {
				line = br.readLine();
				if (line == null) {
					return;
				}
			}
			while (l++ < start + lineNum) {
				line = br.readLine();
				if (line != null) {
					pw.write(line);
					pw.write("\n");
				} else {
					return;
				}
			}
		}
	}

	public void handleEvent(BlockEvent event) {
		LOG.info("Received event: {}", event);
	}

	@RabbitListener(queues = RabbitMQEventDispatcher.QUEUE_WORKSPACE_EXECUTION_EVENT)
	public void handleWorkspaceExecutionEvent(final WorkspaceExecutionEvent event) {
		LOG.info("Workspace event. ID: {}, Status: {}", event.getWorkspaceId(), event.getStatus());

		try {
			updateTaskforceExecutionStatus(event.getTaskforceExecutionId(), event.getStatus());
			if (event.getStatus() == Status.Stopping) {
				WorkspaceExecutor we = workspaceExecutors.get(event.getWorkspaceId());
				we.stop();
			} else if (event.getStatus() == Status.Stopped) {
				workspaceExecutors.remove(event.getWorkspaceId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
