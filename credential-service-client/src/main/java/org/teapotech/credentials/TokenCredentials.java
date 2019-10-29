/**
 * 
 */
package org.teapotech.credentials;

/**
 * @author jiangl
 *
 */
public class TokenCredentials extends CredentialsObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858969723433595754L;
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
