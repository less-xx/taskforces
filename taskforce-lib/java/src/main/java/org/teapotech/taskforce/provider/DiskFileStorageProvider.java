/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangl
 *
 */
public class DiskFileStorageProvider implements FileStorageProvider {

	private static final Logger LOG = LoggerFactory.getLogger(DiskFileStorageProvider.class);

	private final File baseDir;

	public DiskFileStorageProvider(String baseDir) {
		this.baseDir = new File(baseDir);
		if (!this.baseDir.exists()) {
			this.baseDir.mkdirs();
			LOG.info("Created directory {}", this.baseDir);
		}
		LOG.info("Base dir {}", this.baseDir);
	}

	@Override
	public void store(String taskforceId, String key, InputStream in) throws FileStorageException {
		File file = keyToFile(taskforceId, key);
		File folder = file.getParentFile();
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try (FileOutputStream fos = new FileOutputStream(file);) {
			IOUtils.copy(in, fos);
		} catch (Exception e) {
			throw new FileStorageException(e.getMessage(), e);
		}
	}

	@Override
	public InputStream load(String taskforceId, String key) throws FileStorageException {
		File file = keyToFile(taskforceId, key);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new FileStorageException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(String taskforceId, String key) throws FileStorageException {
		File file = keyToFile(taskforceId, key);
		if (!file.exists()) {
			throw new FileStorageException("Cannot find file by taskforce ID: " + taskforceId + ", and key: " + key);
		}
		if (!file.delete()) {
			throw new FileStorageException(
					"Failed to delete file by taskforce ID: " + taskforceId + ", and key: " + key);
		}
	}

	private File keyToFile(String taskforceId, String key) {
		String path = taskforceId;

		String[] segs = key.split("/");
		for (int i = 0; i < segs.length; i++) {
			path = path + File.separator + segs[i];
		}
		return new File(baseDir + File.separator + path);

	}

}
