/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.SimpleTaskforceGroup;

/**
 * @author jiangl
 *
 */
public interface SimpleTaskforceGroupRepo
		extends JpaRepository<SimpleTaskforceGroup, String>, JpaSpecificationExecutor<SimpleTaskforceGroup> {

	SimpleTaskforceGroup findByName(String name);
}
