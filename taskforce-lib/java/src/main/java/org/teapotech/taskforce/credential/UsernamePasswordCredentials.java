/**
 * 
 */
package org.teapotech.taskforce.credential;

/**
 * @author jiangl
 *
 */
public class UsernamePasswordCredentials extends CredentialObject {

	private String username;
	private String password;

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
}
