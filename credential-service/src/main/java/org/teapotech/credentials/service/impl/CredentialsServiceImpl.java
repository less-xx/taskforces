package org.teapotech.credentials.service.impl;

import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.Oauth2Credentials;
import org.teapotech.credentials.UsernamePasswordCredentials;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.AuthenticationMethod;
import org.teapotech.credentials.repo.CredentialsRepo;
import org.teapotech.credentials.service.CredentialsService;
import org.teapotech.credentials.service.exception.CredentialsNotFoundException;
import org.teapotech.security.cipher.ICipher;
import org.teapotech.security.cipher.exception.CipherException;
import org.teapotech.util.JsonHelper;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class CredentialsServiceImpl implements CredentialsService {

	private static Logger LOG = LoggerFactory.getLogger(CredentialsServiceImpl.class);

	@Autowired
	CredentialsRepo credRepo;

	@Autowired
	ICipher cipher;

	@Autowired
	JsonHelper jsonHelper;

	private Credentials encryptCredentials(Credentials cred) throws CipherException {
		String enc = cipher.encrypt(cred.getCredentials());
		Credentials clone = ObjectUtils.clone(cred);
		clone.setCredentials(enc);
		return clone;
	}

	private Credentials decryptCredentials(Credentials cred) throws CipherException {
		String enc = cipher.decrypt(cred.getCredentials());
		Credentials clone = ObjectUtils.clone(cred);
		clone.setCredentials(enc);
		return clone;
	}

	@Transactional
	public Credentials saveCredentials(Credentials cred) throws CipherException {
		Credentials c = encryptCredentials(cred);
		c.setLastUpdatedTime(new Date());
		return credRepo.save(c);
	}

	public Credentials getCredentialsById(String id) {
		return credRepo.findById(id).orElse(null);
	}

	public Credentials getDecryptedCredentialsById(String id) throws CipherException {
		Credentials cred = credRepo.findById(id).orElse(null);
		if (cred == null) {
			throw new CredentialsNotFoundException("Cannot find credentials by id.");
		}
		return decryptCredentials(cred);
	}

	public Credentials createUsernamePasswordCredentials(UsernamePasswordCredentials cred)
			throws JsonProcessingException {
		String credStr = jsonHelper.getJSON(cred);
		Credentials c = new Credentials();
		c.setAuthenticationMethod(AuthenticationMethod.USERNAME_PASSWORD);
		c.setCredentials(credStr);
		return c;
	}

	public Credentials createOauth2Credentials(Oauth2Credentials cred) throws JsonProcessingException {
		String credStr = jsonHelper.getJSON(cred);
		Credentials c = new Credentials();
		c.setAuthenticationMethod(AuthenticationMethod.OAUTH);
		c.setCredentials(credStr);
		return c;
	}

	private <T extends CredentialsObject> T getCredentialObject(Credentials entity, Class<T> objectType)
			throws Exception {
		T t = jsonHelper.getObject(entity.getCredentials(), objectType);
		return t;
	}

	public CredentialsObject getDataSourceCredentials(Credentials entity) throws Exception {

		if (entity.getAuthenticationMethod() == AuthenticationMethod.USERNAME_PASSWORD) {
			return getCredentialObject(entity, UsernamePasswordCredentials.class);
		}

		throw new IllegalArgumentException("Unsupported credential catalog " + entity.getAuthenticationMethod());
	}

}
