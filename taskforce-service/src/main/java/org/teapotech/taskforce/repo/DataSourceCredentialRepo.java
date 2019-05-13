/**
 * 
 */
package org.teapotech.taskforce.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.Credentials;

/**
 * @author jiangl
 *
 */
public interface DataSourceCredentialRepo
		extends JpaRepository<Credentials, String>, JpaSpecificationExecutor<Credentials> {

	List<Credentials> findByIdIn(Collection<String> ids);
}
