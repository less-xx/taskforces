/**
 * 
 */
package org.teapotech.taskforce.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public interface TaskforceExecutionRepo
		extends JpaRepository<TaskforceExecution, Long>, JpaSpecificationExecutor<TaskforceExecution> {

	Page<TaskforceExecution> findByTaskforceId(String taskforceId, Pageable pageable);

	TaskforceExecution findOneByTaskforceIdAndStatusIn(String taskforceId, List<Status> statusList);

	@Transactional
	@Modifying
	@Query("update TaskforceExecution te set te.status=?2, te.lastUpdatedTime=current_timestamp where te.id=?1")
	int updateTaskforceExecutionStatus(Long id, Status status);

	@Transactional
	@Modifying
	@Query("update TaskforceExecution te set te.status='Running', te.startTime=current_timestamp, te.lastUpdatedTime=current_timestamp where te.id=?1")
	int updateTaskforceExecutionStatusRunning(Long id);

	@Transactional
	@Modifying
	@Query("update TaskforceExecution te set te.status='Stopped', te.endTime=current_timestamp, te.lastUpdatedTime=current_timestamp where te.id=?1")
	int updateTaskforceExecutionStatusStopped(Long id);
}
