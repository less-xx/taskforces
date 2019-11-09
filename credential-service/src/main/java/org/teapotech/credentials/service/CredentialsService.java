/**
 * 
 */
package org.teapotech.credentials.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.CredentialType;
import org.teapotech.security.cipher.exception.CipherException;

/**
 * @author jiangl
 *
 */
public interface CredentialsService {

	List<Credentials> getCredentialsByIds(Collection<String> ids);

	CredentialsObject createCredentialsObject(Credentials entity) throws Exception;

	Credentials findByName(String name);

	Credentials saveCredentials(Credentials cred) throws CipherException;

	Credentials getCredentialsById(String id);

	CredentialsObject maskPassword(CredentialsObject cobj);

	Page<Credentials> query(String id, CredentialType type, Boolean enabled, Date lastUpdatedTime, String updatedBy,
			Pageable pageable);
}
