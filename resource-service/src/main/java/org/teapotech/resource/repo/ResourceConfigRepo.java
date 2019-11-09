/**
 * 
 */
package org.teapotech.resource.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.resource.entity.ResourceConfigEntity;

/**
 * @author jiangl
 *
 */
public interface ResourceConfigRepo
		extends JpaRepository<ResourceConfigEntity, String>, JpaSpecificationExecutor<ResourceConfigEntity> {

}
