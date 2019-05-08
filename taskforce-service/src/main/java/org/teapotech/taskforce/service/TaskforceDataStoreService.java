/**
 * 
 */
package org.teapotech.taskforce.service;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;
import org.teapotech.taskforce.repo.TaskforceEntityQuerySpecs;
import org.teapotech.taskforce.repo.TaskforceEntityRepo;
import org.teapotech.taskforce.repo.TaskforceGroupRepo;

/**
 * @author jiangl
 *
 */
@Service
@Transactional
public class TaskforceDataStoreService {

	private static final Logger LOG = LoggerFactory.getLogger(TaskforceDataStoreService.class);

	@Autowired
	TaskforceEntityRepo taskforceEntityRepo;

	@Autowired
	TaskforceGroupRepo groupRepo;

	@PostConstruct
	void init() {
		Page<TaskforceGroup> groups = groupRepo.findAll(PageRequest.of(0, 1));
		if (groups.getTotalElements() == 0) {
			TaskforceGroup exampleGroup = new TaskforceGroup();
			exampleGroup.setName("Example");
			exampleGroup.setDescription("Example taskforces");
			exampleGroup = groupRepo.save(exampleGroup);
			LOG.info("Created example taskforce group.");
		}
	}

	public TaskforceEntity saveTaskforceEntity(TaskforceEntity taskforce) {
		TaskforceEntity t = taskforceEntityRepo.save(taskforce);
		if (StringUtils.isBlank(taskforce.getId())) {
			LOG.info("Created taskforce, ID: {}, Name: {}, Created By: {}", t.getId(), t.getName(), t.getUpdatedBy());
		} else {
			LOG.info("Updated taskforce, ID: {}, Name:{}, Updated By: {}", t.getId(), t.getName(), t.getUpdatedBy());
		}
		return t;
	}

	public TaskforceEntity findByNameAndGroup(String name, TaskforceGroup group) {
		return taskforceEntityRepo.findByNameAndGroup(name, group);
	}

	public Page<TaskforceEntity> findByGroup(TaskforceGroup group, Pageable pageable) {
		return taskforceEntityRepo.findByGroupOrderByName(group, pageable);
	}

	public Page<TaskforceEntity> query(String id, String name, String groupId, Pageable pageable) {
		TaskforceGroup group = findTaskforceGroupById(groupId);
		return taskforceEntityRepo.findAll(TaskforceEntityQuerySpecs.queryTaskforceEntity(id, name, group), pageable);
	}

	public Page<TaskforceGroup> getAllGroups(Pageable pageable) {
		return groupRepo.findAll(pageable);
	}

	public TaskforceEntity findTaskforceEntityById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return taskforceEntityRepo.findById(id).orElse(null);
	}

	public TaskforceGroup findTaskforceGroupById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return groupRepo.findById(id).orElse(null);
	}

}
