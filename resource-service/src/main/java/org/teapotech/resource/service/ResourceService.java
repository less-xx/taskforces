/**
 * 
 */
package org.teapotech.resource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.DBConnectionCredentials;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.service.CredentialsService;
import org.teapotech.resource.ResourceConfig;
import org.teapotech.resource.db.SessionFactoryBuilder;
import org.teapotech.resource.entity.ResourceConfigEntity;
import org.teapotech.resource.entity.ResourceConfigEntity.Type;
import org.teapotech.resource.exception.CreateResourceException;
import org.teapotech.resource.exec.SQLQueryResourceExecutor;
import org.teapotech.resource.repo.ResourceConfigRepo;
import org.teapotech.resource.sql.SQLQueryResource;
import org.teapotech.util.JsonHelper;

/**
 * @author jiangl
 *
 */
@Service
public class ResourceService {

	@Autowired
	CredentialsService credentialService;

	@Autowired
	JsonHelper jsonHelper;

	@Autowired
	ResourceConfigRepo resConfigRepo;

	private List<Credentials> getCredentials(ResourceConfigEntity resConfig) {
		String credentalsIds = resConfig.getCredentialIds();
		if (StringUtils.isBlank(credentalsIds)) {
			return null;
		}
		String[] credIdArray = credentalsIds.split("\\s*,\\s*");
		return credentialService.getCredentialsByIds(Arrays.asList(credIdArray));
	}

	public List<CredentialsObject> getResourceCredentials(ResourceConfigEntity resConfig) throws Exception {
		List<Credentials> creds = getCredentials(resConfig);
		if (creds == null) {
			return null;
		}
		List<CredentialsObject> cobjs = new ArrayList<>();
		for (Credentials c : creds) {
			CredentialsObject cobj = credentialService.createCredentialsObject(c);
			cobjs.add(cobj);
		}
		return cobjs;
	}

	public ResourceConfig<?> createResourceConfig(ResourceConfigEntity resConfig) throws Exception {

		if (resConfig.getType() == Type.SQL_QUERY) {
			SQLQueryResource res = jsonHelper.getObject(resConfig.getConfiguration(), SQLQueryResource.class);
			return res;
		}
		throw new CreateResourceException("Resource type [" + resConfig.getType() + "] is not supported yet.");
	}

	public SQLQueryResourceExecutor createSQLQueryResourceExecutor(SQLQueryResource resource,
			DBConnectionCredentials connCredentials) {
		SessionFactory sessionFactory = SessionFactoryBuilder.getSessionFactory(connCredentials);
		SQLQueryResourceExecutor executor = new SQLQueryResourceExecutor(resource);
		executor.setSessionFactory(sessionFactory);
		return executor;
	}

	@Transactional
	public ResourceConfigEntity saveResourceConfig(ResourceConfigEntity resConfig) throws Exception {
		return resConfigRepo.save(resConfig);
	}

	@Transactional
	public ResourceConfigEntity findByNameAndType(String name, ResourceConfigEntity.Type type) {
		return resConfigRepo.findOneByNameAndType(name, type);
	}

}
