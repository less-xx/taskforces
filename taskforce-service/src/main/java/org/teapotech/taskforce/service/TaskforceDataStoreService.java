/**
 * 
 */
package org.teapotech.taskforce.service;

import static org.teapotech.taskforce.repo.TaskforceEntityQuerySpecs.querySimpleTaskforceEntity;

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
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.SimpleTaskforceGroup;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;
import org.teapotech.taskforce.repo.SimpleTaskforceEntityRepo;
import org.teapotech.taskforce.repo.SimpleTaskforceGroupRepo;
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
	SimpleTaskforceEntityRepo simpleTaskforceEntityRepo;

	@Autowired
	TaskforceGroupRepo groupRepo;

	@Autowired
	SimpleTaskforceGroupRepo simpleGroupRepo;

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

	public TaskforceGroup saveTaskforceGroup(TaskforceGroup group) {
		TaskforceGroup tg = groupRepo.save(group);
		if (StringUtils.isBlank(group.getId())) {
			LOG.info("Created taskforce group, ID: {}, Name: {}, Created By: {}", tg.getId(), tg.getName(),
					tg.getUpdatedBy());
		} else {
			LOG.info("Updated taskforce group, ID: {}, Name:{}, Updated By: {}", tg.getId(), tg.getName(),
					tg.getUpdatedBy());
		}
		return tg;
	}

	public TaskforceEntity findByNameAndGroup(String name, TaskforceGroup group) {
		return taskforceEntityRepo.findByNameAndGroup(name, group);
	}

	public Page<SimpleTaskforceEntity> findByGroup(SimpleTaskforceGroup group, Pageable pageable) {
		return simpleTaskforceEntityRepo.findByGroupOrderByName(group, pageable);
	}

	public Page<SimpleTaskforceEntity> query(String id, String name, String groupId, Pageable pageable) {
		SimpleTaskforceGroup group = findSimpleTaskforceGroupById(groupId);
		return simpleTaskforceEntityRepo.findAll(querySimpleTaskforceEntity(id, name, group), pageable);
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

	public SimpleTaskforceGroup findSimpleTaskforceGroupById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return simpleGroupRepo.findById(id).orElse(null);
	}

	public TaskforceGroup findTaskforceGroupByName(String name) {
		return groupRepo.findByName(name);
	}

	public SimpleTaskforceGroup findSimpleTaskforceGroupByName(String name) {
		return simpleGroupRepo.findByName(name);
	}

}
