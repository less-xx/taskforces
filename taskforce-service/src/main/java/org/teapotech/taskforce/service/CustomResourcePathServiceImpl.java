/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.repo.CustomStorageRepo;
import org.teapotech.taskforce.repo.FileSystemPathRepo;

/**
 * @author lessdev
 *
 */
@Service
@Transactional
public class CustomResourcePathServiceImpl implements CustomResourcePathLoader {

	private static final Logger LOG = LoggerFactory.getLogger(CustomResourcePathServiceImpl.class);
	@Autowired
	CustomStorageRepo custStorageRepo;

	@Autowired
	FileSystemPathRepo fileSystemRepo;

	@Value("${taskforce.file-storage.base-dir}")
	String fileStorageBaseDir;

	@PostConstruct
	void init() {
		FileSystemPath path = fileSystemRepo.findFirstByPath(fileStorageBaseDir);
		if (path == null) {
			path = new FileSystemPath();
			path.setName("File Storage Base Dir");
			path.setPath(fileStorageBaseDir);
			fileSystemRepo.save(path);
			LOG.info("Saved file storage base dir.");
		}
	}

	@Override
	public Collection<FileSystemPath> getAllFileSystemPaths() {
		return fileSystemRepo.findAll();
	}

	@Override
	public FileSystemPath getFileSystemPathById(String id) {
		return fileSystemRepo.findById(id).orElse(null);
	}

	public FileSystemPath saveFileSystemPath(FileSystemPath path) {
		FileSystemPath fpath = fileSystemRepo.save(path);
		if (path.getId() == null) {
			LOG.info("Created file system path, ID: {}, Name: {}", fpath.getId(), fpath.getName());
		} else {
			LOG.info("Updated file system path, ID: {}, Name: {}", fpath.getId(), fpath.getName());
		}
		return fpath;
	}

	public Page<FileSystemPath> getAllFileSystemPaths(Pageable pageable) {
		return fileSystemRepo.findAll(pageable);
	}

	public FileSystemPath findFileSystemPathByName(String name) {
		return fileSystemRepo.findByName(name);
	}
}
