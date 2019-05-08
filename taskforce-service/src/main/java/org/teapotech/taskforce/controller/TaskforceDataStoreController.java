package org.teapotech.taskforce.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;
import org.teapotech.taskforce.exception.TaskforceDataStoreException;
import org.teapotech.taskforce.service.TaskforceDataStoreService;
import org.teapotech.taskforce.web.RestResponse;
import org.teapotech.taskforce.web.SimpleTaskforceEntity;
import org.teapotech.taskforce.web.TaskforceRequest;

@RestController
@CrossOrigin("*")
public class TaskforceDataStoreController extends LogonUserController {

	@Autowired
	TaskforceDataStoreService tfDataStoreService;

	@GetMapping("/taskforce-groups")
	@ResponseBody
	public RestResponse<Page<TaskforceGroup>> getTaskforceGroups(Pageable pageable,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		Page<TaskforceGroup> result = tfDataStoreService.getAllGroups(pageable);
		return new RestResponse<Page<TaskforceGroup>>(result);
	}

	@GetMapping("/taskforces")
	@ResponseBody
	public RestResponse<Page<SimpleTaskforceEntity>> getTaskforces(
			@RequestParam("id") String id,
			@RequestParam("name") String name,
			@RequestParam("group_id") String groupId,
			Pageable pageable,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		Page<TaskforceEntity> result = tfDataStoreService.query(id, name, groupId, pageable);
		List<SimpleTaskforceEntity> entities = result.getContent().stream().map(t -> new SimpleTaskforceEntity(t))
				.collect(Collectors.toList());
		return new RestResponse<Page<SimpleTaskforceEntity>>(
				new PageImpl<>(entities, pageable, result.getTotalElements()));
	}

	@PostMapping("/taskforces")
	@ResponseBody
	public RestResponse<SimpleTaskforceEntity> createTaskforce(@RequestBody TaskforceRequest request,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		TaskforceGroup group = tfDataStoreService.findTaskforceGroupById(request.getGroupId());
		if (group == null) {
			throw new TaskforceDataStoreException("Cannot find taskforce group by id: " + request.getGroupId());
		}
		TaskforceEntity existedTaskfoce = tfDataStoreService.findByNameAndGroup(request.getName(), group);
		if (existedTaskfoce != null) {
			throw new TaskforceDataStoreException("Taskfoce with name [" + request.getName() + "] exists.");
		}
		TaskforceEntity t = new TaskforceEntity();
		t.setConfiguration(request.getConfiguration());
		t.setDescription(request.getDescription());
		t.setName(t.getName());
		t.setGroup(group);
		t.setLastUpdatedTime(new Date());
		t.setUpdatedBy(getLogonUser(httpRequest).getName());
		t = tfDataStoreService.saveTaskforceEntity(t);
		return new RestResponse<SimpleTaskforceEntity>(new SimpleTaskforceEntity(t));
	}

	@PutMapping("/taskforces/{id}")
	public RestResponse<SimpleTaskforceEntity> updateTaskforce(@PathVariable("id") String taskforceId,
			@RequestBody TaskforceRequest request, HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		TaskforceEntity existedTaskfoce = tfDataStoreService.findTaskforceEntityById(taskforceId);
		if (existedTaskfoce == null) {
			throw new TaskforceDataStoreException("Cannot find taskfoce by id [" + taskforceId + "].");
		}
		if (!StringUtils.isBlank(request.getGroupId())) {
			TaskforceGroup group = tfDataStoreService.findTaskforceGroupById(request.getGroupId());
			if (group == null) {
				throw new TaskforceDataStoreException("Cannot find taskforce group by id: " + request.getGroupId());
			}
			existedTaskfoce.setGroup(group);
		}
		existedTaskfoce.setConfiguration(request.getConfiguration());
		existedTaskfoce.setDescription(request.getDescription());
		existedTaskfoce.setName(request.getName());
		existedTaskfoce.setLastUpdatedTime(new Date());
		existedTaskfoce.setUpdatedBy(getLogonUser(httpRequest).getName());
		existedTaskfoce = tfDataStoreService.saveTaskforceEntity(existedTaskfoce);
		return new RestResponse<SimpleTaskforceEntity>(new SimpleTaskforceEntity(existedTaskfoce));
	}
}
