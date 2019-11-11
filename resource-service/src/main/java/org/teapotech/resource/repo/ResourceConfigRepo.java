/**
 * 
 */
package org.teapotech.resource.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.resource.entity.ResourceConfigEntity;
import org.teapotech.resource.entity.ResourceConfigEntity.Type;

/**
 * @author jiangl
 *
 */
public interface ResourceConfigRepo
		extends JpaRepository<ResourceConfigEntity, String>, JpaSpecificationExecutor<ResourceConfigEntity> {

	ResourceConfigEntity findOneByNameAndType(String name, Type type);

}
