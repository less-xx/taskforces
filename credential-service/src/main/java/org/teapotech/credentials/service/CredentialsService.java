/**
 * 
 */
package org.teapotech.credentials.service;

import java.util.Collection;
import java.util.List;

import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.entity.Credentials;
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
}
