/**
 * 
 */
package org.teapotech.taskforce.credential;

/**
 * @author jiangl
 *
 */
public class TokenCredentials extends CredentialObject {

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	private String tokenName = "token";
	private String tokenValue;

}
