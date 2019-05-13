/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.CustomResourcePath;

/**
 * @author jiangl
 *
 */
public interface CustomStorageConfigRepo extends JpaRepository<CustomResourcePath, String>,
		JpaSpecificationExecutor<CustomResourcePath> {

}
