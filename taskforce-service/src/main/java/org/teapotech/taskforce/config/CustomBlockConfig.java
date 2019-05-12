/**
 * 
 */
package org.teapotech.taskforce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.def.CopyFileBlock;
import org.teapotech.taskforce.service.CustomStorageConfigService;

/**
 * @author lessdev
 *
 */
@Configuration
public class CustomBlockConfig {

	@Autowired
	CustomStorageConfigService custStorageConfigService;

	@Bean
	public CopyFileBlock copyFileBlock() {
		CopyFileBlock block = new CopyFileBlock();
		block.setCustomStorageConfigService(custStorageConfigService);
		return block;
	}

}
