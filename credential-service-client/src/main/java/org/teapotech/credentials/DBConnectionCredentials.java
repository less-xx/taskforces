/**
 * 
 */
package org.teapotech.credentials;

/**
 * @author jiangl
 *
 */
public class DBConnectionCredentials extends CredentialsObject {

	private static final long serialVersionUID = 9203677240009576275L;

	public static enum AccessType {
		JDBC, JDBC_MEMORY, JDBC_FILE, JNDI
	}

	private String connectionType;
	private AccessType accessType;
	private String hostname;
	private Integer port;
	private String username;
	private String password;
	private String dbname;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

}
