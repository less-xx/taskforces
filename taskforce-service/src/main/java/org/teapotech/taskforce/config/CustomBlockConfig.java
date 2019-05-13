/**
 * 
 */
package org.teapotech.taskforce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.block.def.file.CopyFileBlock;
import org.teapotech.block.support.CustomResourcePathLoader;

/**
 * @author lessdev
 *
 */
@Configuration
public class CustomBlockConfig {

	@Autowired
	CustomResourcePathLoader custStorageConfigService;

	@Bean
	public CopyFileBlock copyFileBlock() {
		CopyFileBlock block = new CopyFileBlock();
		block.setCustomStorageConfigService(custStorageConfigService);
		return block;
	}

}
