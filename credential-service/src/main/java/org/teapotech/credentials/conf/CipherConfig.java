/**
 * 
 */
package org.teapotech.credentials.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.Key;
import java.security.KeyStore;
import java.util.Properties;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.security.cipher.AESCipher;
import org.teapotech.security.cipher.ICipher;

/**
 * @author jiangl
 *
 */
@Configuration
public class CipherConfig {
	private static Logger LOG = LoggerFactory.getLogger(CipherConfig.class);
	private static final String KEY_ALIAS = "credential-service-key";
	private static final String PROFILE_KEYSTORE_PASS = "KEYSTORE_PASSWORD";
	private static final String PROFILE_KEY_PASS = "KEY_PASSWORD";

	@Value("${cipher.keystore.type}")
	private String keyStoreType;

	@Value("${cipher.keystore.path}")
	private String keyStoreFilePath;

	@Value("${cipher.keystore.password}")
	private String keyStorePass;

	@Autowired
	@Qualifier("credential-service-home-dir")
	File homeDir;

	private synchronized Key loadEncryptionKey() throws Exception {

		File keyStoreFile = new File(keyStoreFilePath);
		File profileFile = new File(homeDir, ".profile");
		KeyStore keystore = KeyStore.getInstance(keyStoreType);
		Properties profile = new Properties();
		if (keyStoreFile.exists() && profileFile.exists()) {
			try (FileInputStream fis = new FileInputStream(keyStoreFile);
					FileInputStream profileIn = new FileInputStream(profileFile);) {
				profile.load(profileIn);
				keystore.load(fis, profile.getProperty(PROFILE_KEYSTORE_PASS).toCharArray());
				Key key = keystore.getKey(KEY_ALIAS, profile.getProperty(PROFILE_KEY_PASS).toCharArray());
				LOG.info("Credential service encryption loaded.");
				return key;
			}
		} else {
			if (keyStoreFile.exists()) {
				keyStoreFile.delete();
			}
			if (profileFile.exists()) {
				profileFile.delete();
			}

			if (SystemUtils.IS_OS_WINDOWS) {
				Files.createFile(keyStoreFile.toPath());
				Files.createFile(profileFile.toPath());
			} else {
				Set<PosixFilePermission> filePermission = PosixFilePermissions.fromString("rw-------");
				FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(filePermission);
				Files.createFile(keyStoreFile.toPath(), permissions);
				Files.createFile(profileFile.toPath(), permissions);
			}
			String keystorePass = RandomStringUtils.randomAscii(16);
			String keyPass = RandomStringUtils.randomAscii(16);
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			SecretKey key = kg.generateKey();
			keystore.load(null, keystorePass.toCharArray());
			keystore.setKeyEntry(KEY_ALIAS, key, keyPass.toCharArray(), null);
			try (FileOutputStream fos = new FileOutputStream(keyStoreFile);) {
				keystore.store(fos, keystorePass.toCharArray());
				LOG.info("Created keystore.");
			}
			profile.setProperty(PROFILE_KEYSTORE_PASS, keystorePass);
			profile.setProperty(PROFILE_KEY_PASS, keyPass);
			try (FileOutputStream fos = new FileOutputStream(profileFile);) {
				profile.store(fos,
						"This file is created by credential service during initialization. \nPlease note it will be overwritten if the credential service is being re-initialized.");
				LOG.info("Created profile");
			}
			return key;
		}
	}

	@Bean
	ICipher cipher() throws Exception {
		return new AESCipher(loadEncryptionKey());
	}
}
