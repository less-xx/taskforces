/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

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
import org.teapotech.block.exception.InvalidWorkspaceException;
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
	public RestResponse<TaskforceExecution> runTaskforce(
			@RequestBody TaskforceExecutionRequest request, HttpServletRequest httpRequest)
			throws InvalidWorkspaceException, TaskforceDataStoreException, TaskforceExecutionException {

		TaskforceEntity taskforce = tfDataStoreService.findTaskforceEntityById(request.getTaskforceId());
		if (taskforce == null) {
			throw new TaskforceDataStoreException("Cannot find taskfoce by id [" + taskforce + "].");
		}

		if (request.getAction() == Action.start) {
			TaskforceExecution te = tfExecutionService.getAliveTaskforceExecutionByTaskforce(taskforce);
			if (te != null) {
				return RestResponse.ok(te);
			}
			te = tfExecutionService.executeWorkspace(taskforce);
			return RestResponse.ok(te);
		} else {
			TaskforceExecution te = tfExecutionService.getAliveTaskforceExecutionByTaskforce(taskforce);
			if (te == null) {
				throw new TaskforceExecutionException("No alive execution for taskforce " + taskforce.getId());
			}
			tfExecutionService.stopTaskfroceExecution(te);
			return RestResponse.ok(te);
		}

	}

	@GetMapping("/taskforce-executions/{id}")
	public RestResponse<TaskforceExecution> queryById(@PathVariable("id") Long id) {
		TaskforceExecution te = tfExecutionService.getTaskforceExecutionById(id);
		if (te == null) {
			throw new EntityNotFoundException("Cannot find taskforce execution by id: " + id);
		}
		return RestResponse.ok(te);
	}

	@GetMapping("/taskforce-executions")
	public RestResponse<Page<TaskforceExecutionWrapper>> queryTaskforceExecutions(
			@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "taskforce_id", required = false) String taskforceId,
			@RequestParam(name = "status", required = false) List<Status> status,
			@RequestParam(name = "created_time", required = false) Date createdTime,
			@RequestParam(name = "created_by", required = false) String createdBy,
			Pageable pageable) {
		Page<TaskforceExecution> results = tfExecutionService.query(id, taskforceId, status, createdTime, createdBy,
				pageable);
		List<TaskforceExecutionWrapper> list = results.getContent().stream()
				.map(te -> new TaskforceExecutionWrapper(te)).collect(Collectors.toList());

		return RestResponse.ok(new PageImpl<>(list, pageable, results.getTotalElements()));
	}

}
