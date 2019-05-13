/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.taskforce.entity.CustomResourcePath;
import org.teapotech.taskforce.repo.CustomStorageConfigRepo;

/**
 * @author lessdev
 *
 */
@Service
@Transactional
public class CustomResourcePathServiceImpl implements CustomResourcePathLoader {

	@Autowired
	CustomStorageConfigRepo custStorageConfigRepo;

	@Override
	public List<CustomResourcePath> getAllCustomResourcePaths() {
		return custStorageConfigRepo.findAll();
	}
}
