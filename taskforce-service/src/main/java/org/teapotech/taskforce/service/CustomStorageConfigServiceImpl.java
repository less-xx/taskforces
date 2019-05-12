/**
 * 
 */
package org.teapotech.taskforce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teapotech.taskforce.entity.CustomStorageConfigEntity;
import org.teapotech.taskforce.repo.CustomStorageConfigRepo;

/**
 * @author lessdev
 *
 */
@Service
@Transactional
public class CustomStorageConfigServiceImpl implements CustomStorageConfigService {

	@Autowired
	CustomStorageConfigRepo custStorageConfigRepo;

	@Override
	public List<CustomStorageConfigEntity> getAllCustomStorageConfigs() {
		return custStorageConfigRepo.findAll();
	}
}
