package org.teapotech.taskforce.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.SimpleTaskforceGroup;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;
import org.teapotech.taskforce.exception.TaskforceDataStoreException;
import org.teapotech.taskforce.service.TaskforceDataStoreService;
import org.teapotech.taskforce.web.RestResponse;
import org.teapotech.taskforce.web.TaskforceGroupRequest;
import org.teapotech.taskforce.web.TaskforceRequest;

@RestController
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

	@PostMapping("/taskforce-groups")
	@ResponseBody
	public RestResponse<SimpleTaskforceGroup> createTaskforceGroup(@RequestBody TaskforceGroupRequest request,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {
		TaskforceGroup group = tfDataStoreService.findTaskforceGroupByName(request.getName());
		if (group != null) {
			throw new TaskforceDataStoreException("Taskforce group name exists.");
		}
		group = new TaskforceGroup();
		group.setName(request.getName());
		group.setDescription(request.getDescription());
		group.setLastUpdatedTime(new Date());
		group.setUpdatedBy(getLogonUser(httpRequest).getName());
		group = tfDataStoreService.saveTaskforceGroup(group);
		return new RestResponse<SimpleTaskforceGroup>(group.toSimple());

	}

	@PutMapping("/taskforce-groups/{id}")
	@ResponseBody
	public RestResponse<SimpleTaskforceGroup> updateTaskforceGroup(
			@PathVariable("id") String groupId,
			@RequestBody TaskforceGroupRequest request,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {
		TaskforceGroup group = tfDataStoreService.findTaskforceGroupById(groupId);
		if (group == null) {
			throw new TaskforceDataStoreException("Cannot find taskforce group by id: " + groupId);
		}

		TaskforceGroup existedGroup = tfDataStoreService.findTaskforceGroupByName(request.getName());
		if (existedGroup != null && !existedGroup.getId().equals(groupId)) {
			throw new TaskforceDataStoreException("Taskforce group with name " + request.getName() + " exists.");
		}
		group.setName(request.getName());
		group.setDescription(request.getDescription());
		group.setLastUpdatedTime(new Date());
		group.setUpdatedBy(getLogonUser(httpRequest).getName());
		group = tfDataStoreService.saveTaskforceGroup(group);
		return new RestResponse<SimpleTaskforceGroup>(group.toSimple());

	}

	@GetMapping("/taskforces")
	@ResponseBody
	public RestResponse<Page<SimpleTaskforceEntity>> getTaskforces(
			@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "group_id", required = false) String groupId,
			Pageable pageable,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		Page<SimpleTaskforceEntity> result = tfDataStoreService.query(id, name, groupId, pageable);
		return new RestResponse<Page<SimpleTaskforceEntity>>(result);
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
		t.setName(request.getName());
		t.setGroup(group);
		t.setLastUpdatedTime(new Date());
		t.setUpdatedBy(getLogonUser(httpRequest).getName());
		t = tfDataStoreService.saveTaskforceEntity(t);
		return new RestResponse<SimpleTaskforceEntity>(t.toSimple());
	}

	@PutMapping("/taskforces/{id}")
	public RestResponse<SimpleTaskforceEntity> updateTaskforce(@PathVariable("id") String taskforceId,
			@RequestBody TaskforceRequest request, HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		TaskforceEntity existedTaskfoce = tfDataStoreService.findTaskforceEntityById(taskforceId);
		if (existedTaskfoce == null) {
			throw new TaskforceDataStoreException("Cannot find taskfoce by id [" + taskforceId + "].");
		}

		// update group
		if (!StringUtils.isBlank(request.getGroupId())) {
			TaskforceGroup group = tfDataStoreService.findTaskforceGroupById(request.getGroupId());
			if (group == null) {
				throw new TaskforceDataStoreException("Cannot find taskforce group by id: " + request.getGroupId());
			}
			existedTaskfoce.setGroup(group);
		}

		// update name
		if (!StringUtils.isBlank(request.getName())) {
			TaskforceEntity sameNameTaskforce = tfDataStoreService.findByNameAndGroup(request.getName(),
					existedTaskfoce.getGroup());
			if (sameNameTaskforce != null && !sameNameTaskforce.getId().equals(taskforceId)) {
				throw new TaskforceDataStoreException("Taskfoce with name [" + request.getName() + "] exists.");
			}
			existedTaskfoce.setName(request.getName());
		}

		// update configuration
		if (!StringUtils.isBlank(request.getConfiguration())) {
			existedTaskfoce.setConfiguration(request.getConfiguration());
		}
		// update description
		if (!StringUtils.isBlank(request.getDescription())) {
			existedTaskfoce.setDescription(request.getDescription());
		}

		existedTaskfoce.setLastUpdatedTime(new Date());
		existedTaskfoce.setUpdatedBy(getLogonUser(httpRequest).getName());
		existedTaskfoce = tfDataStoreService.saveTaskforceEntity(existedTaskfoce);
		return new RestResponse<SimpleTaskforceEntity>(existedTaskfoce.toSimple());
	}

	@GetMapping("/taskforces/{id}")
	public RestResponse<TaskforceEntity> getTaskforce(@PathVariable("id") String taskforceId,
			HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

		TaskforceEntity existedTaskfoce = tfDataStoreService.findTaskforceEntityById(taskforceId);
		if (existedTaskfoce == null) {
			throw new TaskforceDataStoreException("Cannot find taskfoce by id [" + taskforceId + "].");
		}
		return new RestResponse<TaskforceEntity>(existedTaskfoce);
	}
}
