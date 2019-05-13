/**
 * 
 */
package org.teapotech.taskforce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangl
 *
 */
public class KeyStoreUtils {

	private static Logger LOG = LoggerFactory.getLogger(KeyStoreUtils.class);

	public static void loadKeyStore(File keyStoreFile, KeyStore keyStore, char[] keyStorePassword)
			throws Exception {
		if (keyStoreFile == null) {
			keyStore.load(null, keyStorePassword);
			LOG.info("Initialize empty key store.");
		} else {
			try (FileInputStream in = new FileInputStream(keyStoreFile);) {
				keyStore.load(in, keyStorePassword);
				LOG.info("Loaded key store from {}", keyStoreFile.getAbsolutePath());
			}
		}
	}

	public static void saveKeyStore(File keyStoreFile, KeyStore keyStore, char[] keyStorePassword) throws Exception {
		try (FileOutputStream out = new FileOutputStream(keyStoreFile);) {
			keyStore.store(out, keyStorePassword);
			LOG.info("Saved key store file at {}", keyStoreFile.getAbsolutePath());
		}
	}
}
