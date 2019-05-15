/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	CustomStorageRepo custStorageRepo;

	@Autowired
	FileSystemPathRepo fileSystemRepo;

	@Override
	public Collection<FileSystemPath> getAllFileSystemPaths() {
		return fileSystemRepo.findAll();
	}

	@Override
	public FileSystemPath getFileSystemPathById(String id) {
		return fileSystemRepo.findById(id).orElse(null);
	}
}
