/**
 * 
 */
package org.teapotech.taskforce.expert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author jiangl
 *
 */
@Service
public class FileScanner {

	private static Logger LOG = LoggerFactory.getLogger(FileScanner.class);


	public File[] getFiles(String path) throws IOException {

		LOG.info("Try to scann files at {}", path);

		File parent = new File(path);
		if (!parent.exists()) {
			throw new FileNotFoundException(path + " does not exist.");
		}
		return parent.listFiles();
	}
}
