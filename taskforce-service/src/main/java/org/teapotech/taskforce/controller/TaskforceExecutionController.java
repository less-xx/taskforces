/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.base.controller.LogonUserController;
import org.teapotech.block.exception.InvalidWorkspaceException;
import org.teapotech.block.executor.BlockExecutionProgress;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;
import org.teapotech.taskforce.exception.TaskforceDataStoreException;
import org.teapotech.taskforce.exception.TaskforceExecutionException;
import org.teapotech.taskforce.service.TaskforceDataStoreService;
import org.teapotech.taskforce.service.TaskforceExecutionService;
import org.teapotech.taskforce.web.RestResponse;
import org.teapotech.taskforce.web.TaskforceExecutionRequest;
import org.teapotech.taskforce.web.TaskforceExecutionRequest.Action;
import org.teapotech.taskforce.web.TaskforceExecutionWrapper;

/**
 * @author jiangl
 *
 */
@RestController
public class TaskforceExecutionController extends LogonUserController {

	@Autowired
	TaskforceDataStoreService tfDataStoreService;

	@Autowired
	TaskforceExecutionService tfExecutionService;

	@PostMapping("/taskforce-executions")
	public RestResponse<TaskforceExecutionWrapper> runTaskforce(
			@RequestBody TaskforceExecutionRequest request, HttpServletRequest httpRequest)
			throws InvalidWorkspaceException, TaskforceDataStoreException, TaskforceExecutionException {

		TaskforceEntity taskforce = tfDataStoreService.findTaskforceEntityById(request.getTaskforceId());
		if (taskforce == null) {
			throw new TaskforceDataStoreException("Cannot find taskfoce by id [" + taskforce + "].");
		}

		if (request.getAction() == Action.start) {
			TaskforceExecution te = tfExecutionService.getAliveTaskforceExecutionByTaskforce(taskforce);
			if (te == null) {
				te = tfExecutionService.executeWorkspace(taskforce);
			}
			Collection<BlockExecutionProgress> progress = tfExecutionService.getBlockExecutionProgress(te);
			return RestResponse.ok(new TaskforceExecutionWrapper(te, progress));
		} else {
			TaskforceExecution te = tfExecutionService.getAliveTaskforceExecutionByTaskforce(taskforce);
			if (te == null) {
				throw new TaskforceExecutionException("No alive execution for taskforce " + taskforce.getId());
			}
			tfExecutionService.stopTaskfroceExecution(te);
			Collection<BlockExecutionProgress> progress = tfExecutionService.getBlockExecutionProgress(te);
			return RestResponse.ok(new TaskforceExecutionWrapper(te, progress));
		}

	}

	@GetMapping("/taskforce-executions/{id}")
	public RestResponse<TaskforceExecutionWrapper> queryById(@PathVariable("id") Long id) {
		TaskforceExecution te = tfExecutionService.getTaskforceExecutionById(id);
		if (te == null) {
			throw new EntityNotFoundException("Cannot find taskforce execution by id: " + id);
		}
		Collection<BlockExecutionProgress> progress = tfExecutionService.getBlockExecutionProgress(te);
		return RestResponse.ok(new TaskforceExecutionWrapper(te, progress));
	}

	@GetMapping("/taskforce-executions")
	public RestResponse<Page<TaskforceExecutionWrapper>> queryTaskforceExecutions(
			@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "taskforce_id", required = false) String taskforceId,
			@RequestParam(name = "status", required = false) List<Status> status,
			@RequestParam(name = "start_time", required = false) Date startTime,
			@RequestParam(name = "start_by", required = false) String startBy,
			Pageable pageable) {
		Page<TaskforceExecution> results = tfExecutionService.query(id, taskforceId, status, startTime, startBy,
				pageable);
		List<TaskforceExecutionWrapper> list = results.getContent().stream()
				.map(te -> {
					Collection<BlockExecutionProgress> progress = tfExecutionService.getBlockExecutionProgress(te);
					return new TaskforceExecutionWrapper(te, progress);
				}).collect(Collectors.toList());

		return RestResponse.ok(new PageImpl<>(list, pageable, results.getTotalElements()));
	}

	@GetMapping("/taskforce-executions/{id}/logs")
	public void readTaskforceExecutionLog(@PathVariable("id") Long id,
			@RequestParam(name = "start", required = false, defaultValue = "0") int start,
			@RequestParam(name = "lines", required = false, defaultValue = "100") int lineNum,
			HttpServletResponse response) throws Exception {
		TaskforceExecution te = tfExecutionService.getTaskforceExecutionById(id);
		if (te == null) {
			throw new EntityNotFoundException("Cannot find taskforce execution by id: " + id);
		}
		tfExecutionService.readLogFileContent(te, start, lineNum, response.getOutputStream());
	}

}
