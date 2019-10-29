package org.teapotech.credentials.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("credentials-service")
public class CredentialsServiceConfigurationProperties {

	private boolean encryptCredentials;
	private String homeDir;

	public boolean isEncryptCredentials() {
		return encryptCredentials;
	}

	public void setEncryptCredentials(boolean encryptCredentials) {
		this.encryptCredentials = encryptCredentials;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

}
