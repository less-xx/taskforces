/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.taskforce.dto.BlockRegistryDTO;
import org.teapotech.taskforce.service.BlockRegistryService;

/**
 * @author jiangl
 *
 */
@RestController
public class BlockRegistryController {

	@Autowired
	BlockRegistryService blockRegistryService;

	@GetMapping("/block-registries")
	@ResponseBody
	public Map<String, Set<BlockRegistryDTO>> getAllBlockRegistries() throws Exception {
		return blockRegistryService.getCategorizedBlockRegistries();
	}
}
