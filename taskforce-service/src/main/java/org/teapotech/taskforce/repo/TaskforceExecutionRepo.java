/**
 * 
 */
package org.teapotech.taskforce.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public interface TaskforceExecutionRepo
		extends JpaRepository<TaskforceExecution, Long>, JpaSpecificationExecutor<TaskforceExecution> {

	Page<TaskforceExecution> findByTaskforce(SimpleTaskforceEntity taskforce, Pageable pageable);

	TaskforceExecution findOneByTaskforceAndStatusIn(SimpleTaskforceEntity taskforce, List<Status> statusList);

	@Transactional
	@Modifying
	@Query("update TaskforceExecution te set te.status=?2, te.lastUpdatedTime=?3 where te.id=?1")
	int updateTaskforceExecution(Long id, Status status, Date updateTime);
}
