/**
 * 
 */
package org.teapotech.resource.db;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.resource.db.DatabaseConnectionSpec.Access;
import org.teapotech.resource.db.DatabaseConnectionSpec.JDBC_Access;
import org.teapotech.util.ObjectValueExtractor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;

/**
 * @author jiangl
 *
 */
public class SessionFactoryBuilder {

	private static Logger LOG = LoggerFactory.getLogger(SessionFactoryBuilder.class);

	private static Map<String, DatabaseConnectionSpec> allDatabaseConnectionSpecs = new DatabaseConnectionSpecLoader()
			.getAllSpecs();
	private static Cache<String, SessionFactory> sessionFactoryCache = CacheBuilder.newBuilder()
			.expireAfterAccess(1, TimeUnit.HOURS).maximumSize(100).build();

	/**
	 * 
	 * @param connectionConfig
	 * @param credentials
	 * @return
	 */
	public static SessionFactory getSessionFactory(DatabaseConnectionConfig connectionConfig) {

		SessionFactory fac = sessionFactoryCache.getIfPresent(getIdentifier(connectionConfig));
		if (fac != null) {
			return fac;
		}

		DatabaseConnectionSpec spec = allDatabaseConnectionSpecs.get(connectionConfig.getConnectionType());
		if (spec == null) {
			throw new EntityNotFoundException(
					"Cannot find database connection spec by connection type " + connectionConfig.getConnectionType());
		}

		fac = buildSessionFactory(connectionConfig, spec);
		sessionFactoryCache.put(getIdentifier(connectionConfig), fac);
		return fac;
	}

	public static void invalidateSessionFactoryCacheById(String credentialId) {
		sessionFactoryCache.invalidate(credentialId);
		LOG.debug("Invalidated session faction by credential id: {}", credentialId);
	}

	/**
	 * 
	 * @param jdbcCred
	 * @param spec
	 * @return
	 */
	public static SessionFactory buildSessionFactory(final DatabaseConnectionConfig connConfig,
			final DatabaseConnectionSpec spec) {

		Optional<Access> opAccess = spec.getAccess().stream()
				.filter(a -> a.getType().equals(connConfig.getAccessType().toString())).findFirst();
		if (!opAccess.isPresent()) {
			throw new HibernateException("Cannot find access by type " + connConfig.getAccessType());
		}
		if (!ClassUtils.isAssignable(opAccess.get().getClass(), JDBC_Access.class)) {
			throw new HibernateException("Only JDBC_ACCESS type is supported.");
		}

		JDBC_Access jdbcAccess = (JDBC_Access) opAccess.get();
		Map<String, Object> settingValues;
		try {
			settingValues = ObjectValueExtractor.toMap(connConfig);
		} catch (Exception e) {
			throw new HibernateException("Invalid connection settings.", e);
		}
		Map<String, Object> mergeSettingValues = jdbcAccess.getParameters().stream().collect(Collectors
				.toMap(DatabaseConnectionParameter::getId, new Function<DatabaseConnectionParameter, Object>() {
					@Override
					public Object apply(DatabaseConnectionParameter t) {

						Object value = settingValues.get(t.getId());
						if (value == null) {
							value = t.getDefaultValue();
						}
						if (value == null && t.isRequired()) {
							throw new HibernateException("Value of " + t.getId() + " is required.");
						}
						return value;
					}
				}));

		Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.dialect", (String) mergeSettingValues.get(JDBC_Access.PARAM_DIALECT));
		configuration.setProperty("hibernate.connection.driver_class",
				(String) mergeSettingValues.get(JDBC_Access.PARAM_DRIVER_CLASS));

		configuration.setProperty("hibernate.connection.pool_size", "5");
		configuration.setProperty("hibernate.c3p0.min_size", "5");
		configuration.setProperty("hibernate.c3p0.max_size", "20");

		configuration.setProperty("hibernate.current_session_context_class", "thread");
		configuration.setProperty("hibernate.show_sql", "true");
		configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

		if (connConfig.getAccessType() == DatabaseConnectionConfig.AccessType.JDBC
				|| connConfig.getAccessType() == DatabaseConnectionConfig.AccessType.JDBC_MEMORY
				|| connConfig.getAccessType() == DatabaseConnectionConfig.AccessType.JDBC_FILE) {
			configuration.setProperty("hibernate.connection.url",
					getJdbcUrl(mergeSettingValues, jdbcAccess.getUrlPattern()));
			String username = (String) mergeSettingValues.get(JDBC_Access.PARAM_USERNAME);
			if (username != null) {
				configuration.setProperty("hibernate.connection.username", username);
			}
			String password = (String) mergeSettingValues.get(JDBC_Access.PARAM_PASSWORD);
			if (password != null) {
				configuration.setProperty("hibernate.connection.password", password);
			}
			return configuration.buildSessionFactory();
		} else if (connConfig.getAccessType() == DatabaseConnectionConfig.AccessType.JNDI) {
			configuration.setProperty("hibernate.connection.datasource",
					(String) mergeSettingValues.get(JDBC_Access.PARAM_JNDI_NAME));
			return configuration.buildSessionFactory();
		}
		throw new HibernateException("Unsupported access type " + connConfig.getAccessType());
	}

	protected static String getJdbcUrl(Map<String, Object> valueMap, String urlPattern) {
		return StringSubstitutor.replace(urlPattern, valueMap);
	}

	private static String getIdentifier(DatabaseConnectionConfig dcConf) {
		String input = StringUtils.join(new String[] { dcConf.getAccessType().name(), dcConf.getConnectionType(),
				dcConf.getDbname(), dcConf.getHostname(), String.valueOf(dcConf.getPort()), dcConf.getUsername(),
				dcConf.getPassword() }, "||");
		return Hashing.sha256().hashString(input, Charset.defaultCharset()).toString();
	}
}
