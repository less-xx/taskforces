/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;

/**
 * @author jiangl
 *
 */
public interface TaskforceEntityRepo
		extends JpaRepository<TaskforceEntity, String>, JpaSpecificationExecutor<TaskforceEntity> {

	TaskforceEntity findByNameAndGroup(String name, TaskforceGroup group);

	Page<TaskforceEntity> findByGroupOrderByName(TaskforceGroup group, Pageable pageable);
}
