/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public interface TaskforceExecutionRepo
		extends JpaRepository<TaskforceExecution, String>, JpaSpecificationExecutor<TaskforceExecution> {

	Page<TaskforceExecution> findByTaskforce(TaskforceEntity taskforce, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update TaskforceExecution te set te.status=?2 where te.id=?1")
	int updateTaskforceExecution(String id, Status status);
}
