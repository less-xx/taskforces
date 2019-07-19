/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.SimpleTaskforceEntity;
import org.teapotech.taskforce.entity.SimpleTaskforceGroup;

/**
 * @author jiangl
 *
 */
public interface SimpleTaskforceEntityRepo
		extends JpaRepository<SimpleTaskforceEntity, String>, JpaSpecificationExecutor<SimpleTaskforceEntity> {

	SimpleTaskforceEntity findByNameAndGroup(String name, SimpleTaskforceGroup group);

	Page<SimpleTaskforceEntity> findByGroupOrderByName(SimpleTaskforceGroup group, Pageable pageable);
}
