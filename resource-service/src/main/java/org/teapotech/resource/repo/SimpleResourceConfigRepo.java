/**
 * 
 */
package org.teapotech.resource.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.resource.entity.SimpleResourceConfig;

/**
 * @author jiangl
 *
 */
public interface SimpleResourceConfigRepo
		extends JpaRepository<SimpleResourceConfig, String>, JpaSpecificationExecutor<SimpleResourceConfig> {

}
