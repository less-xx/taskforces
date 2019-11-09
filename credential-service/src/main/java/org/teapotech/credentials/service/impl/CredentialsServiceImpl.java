package org.teapotech.credentials.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.DBConnectionCredentials;
import org.teapotech.credentials.Oauth2Credentials;
import org.teapotech.credentials.UsernamePasswordCredentials;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.CredentialType;
import org.teapotech.credentials.repo.CredentialsQuerySpecs;
import org.teapotech.credentials.repo.CredentialsRepo;
import org.teapotech.credentials.service.CredentialsService;
import org.teapotech.security.cipher.ICipher;
import org.teapotech.security.cipher.exception.CipherException;
import org.teapotech.util.JsonHelper;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class CredentialsServiceImpl implements CredentialsService {

	private static Logger LOG = LoggerFactory.getLogger(CredentialsServiceImpl.class);
	public final static String MASKED_PASSWORD_VALUES = "************";

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

	@Override
	@Transactional
	public List<Credentials> getCredentialsByIds(Collection<String> ids) {
		return credRepo.findByIdIn(ids);
	}

	@Override
	@Transactional
	public Credentials saveCredentials(Credentials cred) throws CipherException {
		Credentials c = encryptCredentials(cred);
		c.setLastUpdatedTime(new Date());
		return credRepo.save(c);
	}

	@Override
	@Transactional
	public Credentials getCredentialsById(String id) {
		return credRepo.findById(id).orElse(null);
	}

	@Transactional
	public Credentials getDecryptedCredentialsById(String id) throws CipherException {
		Credentials cred = credRepo.findById(id).orElse(null);
		if (cred == null) {
			throw new EntityNotFoundException("Cannot find credentials by id.");
		}
		return decryptCredentials(cred);
	}

	public Credentials createUsernamePasswordCredentials(UsernamePasswordCredentials cred)
			throws JsonProcessingException {
		String credStr = jsonHelper.getJSON(cred);
		Credentials c = new Credentials();
		c.setType(CredentialType.USERNAME_PASSWORD);
		c.setCredentials(credStr);
		return c;
	}

	public Credentials createOauth2Credentials(Oauth2Credentials cred) throws JsonProcessingException {
		String credStr = jsonHelper.getJSON(cred);
		Credentials c = new Credentials();
		c.setType(CredentialType.OAUTH);
		c.setCredentials(credStr);
		return c;
	}

	public Credentials createDBConnectionCredentials(DBConnectionCredentials cred) throws JsonProcessingException {
		String credStr = jsonHelper.getJSON(cred);
		Credentials c = new Credentials();
		c.setType(CredentialType.DB_CONNECTION);
		c.setCredentials(credStr);
		return c;
	}

	private <T extends CredentialsObject> T createCredentialsObject(Credentials entity, Class<T> objectType)
			throws Exception {
		Credentials dc = decryptCredentials(entity);
		T t = jsonHelper.getObject(dc.getCredentials(), objectType);
		return t;
	}

	@Override
	public CredentialsObject createCredentialsObject(Credentials entity) throws Exception {

		if (entity.getType() == CredentialType.USERNAME_PASSWORD) {
			return createCredentialsObject(entity, UsernamePasswordCredentials.class);
		}

		if (entity.getType() == CredentialType.DB_CONNECTION) {
			return createCredentialsObject(entity, DBConnectionCredentials.class);
		}

		throw new IllegalArgumentException("Unsupported credential type " + entity.getType());
	}

	public CredentialsObject maskPassword(CredentialsObject cobj) {
		if (cobj instanceof UsernamePasswordCredentials) {
			((UsernamePasswordCredentials) cobj).setPassword(MASKED_PASSWORD_VALUES);
		} else if (cobj instanceof DBConnectionCredentials) {
			((DBConnectionCredentials) cobj).setPassword(MASKED_PASSWORD_VALUES);
		}
		return cobj;
	}

	@Override
	public Credentials findByName(String name) {
		return credRepo.findOneByName(name);
	}

	@Override
	@Transactional
	public Page<Credentials> query(String id, CredentialType type, Boolean enabled, Date lastUpdatedTime,
			String updatedBy, Pageable pageable) {
		return credRepo.findAll(CredentialsQuerySpecs.queryCredentials(id, type, enabled, lastUpdatedTime, updatedBy),
				pageable);
	}
}
