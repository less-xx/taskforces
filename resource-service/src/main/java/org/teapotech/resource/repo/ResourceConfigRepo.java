/**
 * 
 */
package org.teapotech.resource.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.resource.entity.ResourceConfig;

/**
 * @author jiangl
 *
 */
public interface ResourceConfigRepo
		extends JpaRepository<ResourceConfig, String>, JpaSpecificationExecutor<ResourceConfig> {

}
