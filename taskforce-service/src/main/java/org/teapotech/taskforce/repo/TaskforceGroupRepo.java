/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.TaskforceGroup;

/**
 * @author jiangl
 *
 */
public interface TaskforceGroupRepo
		extends JpaRepository<TaskforceGroup, String>, JpaSpecificationExecutor<TaskforceGroup> {

}
