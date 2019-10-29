/**
 * 
 */
package org.teapotech.credentials.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.teapotech.credentials.entity.Credentials;

/**
 * @author jiangl
 *
 */
public interface CredentialsRepo
		extends JpaRepository<Credentials, String>, JpaSpecificationExecutor<Credentials> {

	List<Credentials> findByIdIn(Collection<String> ids);

	@Modifying
	@Query(nativeQuery = true, value = "INSERT into {h-schema}credentials ()")
	Credentials saveCredentials(Credentials cred, String pgCred);
}
