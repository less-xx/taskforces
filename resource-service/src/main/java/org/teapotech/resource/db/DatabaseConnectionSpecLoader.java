/**
 * 
 */
package org.teapotech.resource.db;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.teapotech.resource.db.DatabaseConnectionSpec.Access;
import org.teapotech.util.JsonHelper;

/**
 * @author jiangl
 *
 */
public class DatabaseConnectionSpecLoader {

	private static Logger LOG = LoggerFactory.getLogger(DatabaseConnectionSpecLoader.class);
	private static JsonHelper jsonHelper = JsonHelper.newInstance().build();

	private Map<String, DatabaseConnectionSpec> allSpecs;

	public DatabaseConnectionSpecLoader() {
		this.loadAll();
	}

	private void loadAll() {

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources("database-connection-spec/*.json");
			Map<String, DatabaseConnectionSpec> map = new HashMap<>();
			for (Resource res : resources) {
				try {
					DatabaseConnectionSpec spec = jsonHelper.getObject(res.getInputStream(),
							DatabaseConnectionSpec.class);
					map.put(spec.getConnectionType(), spec);
				} catch (Exception e) {
					LOG.error("Invalid database connection spec. {}", e.getMessage());
				}
			}
			this.allSpecs = Collections.unmodifiableMap(map);
		} catch (IOException ie) {
			LOG.error(ie.getMessage(), ie);
		}
	}

	public Map<String, DatabaseConnectionSpec> getAllSpecs() {
		return this.allSpecs;
	}

	public DatabaseConnectionSpec getSpec(String connectionType) {
		return this.allSpecs.get(connectionType);
	}

	public Access getAccess(String connectionType, String accessType) {
		DatabaseConnectionSpec spec = getSpec(connectionType);
		if (spec == null) {
			return null;
		}
		Optional<Access> o = spec.getAccess().stream().filter(a -> a.getType().equals(accessType)).findFirst();
		if (!o.isPresent()) {
			return null;
		}
		return o.get();
	}
}
