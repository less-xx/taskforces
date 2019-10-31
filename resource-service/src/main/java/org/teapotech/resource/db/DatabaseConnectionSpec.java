/**
 * 
 */
package org.teapotech.resource.db;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author jiangl
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseConnectionSpec {

	@JsonProperty("connection_type")
	private String connectionType;

	private List<Access> access;

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(List<Access> access) {
		this.access = access;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = Access.class)
	@JsonSubTypes({ @JsonSubTypes.Type(value = JDBC_Access.class, name = JDBC_Access.TYPE),
			@JsonSubTypes.Type(value = JDBC_MEMORY_Access.class, name = JDBC_MEMORY_Access.TYPE),
			@JsonSubTypes.Type(value = JDBC_FILE_Access.class, name = JDBC_FILE_Access.TYPE),
			@JsonSubTypes.Type(value = JNDI_Access.class, name = JNDI_Access.TYPE) })
	abstract public static class Access {
		private List<DatabaseConnectionParameter> parameters;

		abstract public String getType();

		public List<DatabaseConnectionParameter> getParameters() {
			return parameters;
		}

		public void setParameters(List<DatabaseConnectionParameter> parameters) {
			this.parameters = parameters;
		}

		public Optional<DatabaseConnectionParameter> findParameterById(String id) {
			if (parameters == null) {
				return Optional.empty();
			}
			return parameters.stream().filter(p -> p.getId().equals(id)).findFirst();
		}
	}

	public static class JDBC_Access extends Access {

		public final static String TYPE = "JDBC";

		public final static String PARAM_USERNAME = "username";
		public final static String PARAM_PASSWORD = "password";
		public final static String PARAM_DRIVER_CLASS = "driver_class";
		public final static String PARAM_DIALECT = "dialect";
		public final static String PARAM_JNDI_NAME = "jndi_name";

		@JsonProperty("url_pattern")
		private String urlPattern;

		public String getUrlPattern() {
			return urlPattern;
		}

		@Override
		public String getType() {
			return JDBC_Access.TYPE;
		}

	}

	public static class JDBC_MEMORY_Access extends JDBC_Access {

		public final static String TYPE = "JDBC_MEMORY";

		@Override
		public String getType() {
			return JDBC_MEMORY_Access.TYPE;
		}
	}

	public static class JDBC_FILE_Access extends JDBC_Access {

		public final static String TYPE = "JDBC_FILE";

		@Override
		public String getType() {
			return JDBC_FILE_Access.TYPE;
		}
	}

	public static class JNDI_Access extends Access {

		public final static String TYPE = "JNDI";

		@Override
		public String getType() {
			return JNDI_Access.TYPE;
		}

	}
}
