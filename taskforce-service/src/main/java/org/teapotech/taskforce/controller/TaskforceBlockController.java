/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.block.model.Block;
import org.teapotech.taskforce.dto.BlockDefinitionDTO;
import org.teapotech.taskforce.service.BlockRegistryService;

/**
 * @author jiangl
 *
 */
@RestController
@CrossOrigin("*")
public class TaskforceBlockController extends LogonUserController {

	@Autowired
	BlockRegistryService blockRegistryService;

	@GetMapping("/custom-block-definitions")
	@ResponseBody
	public List<BlockDefinitionDTO> getAllBlockDefinitions() throws Exception {
		return blockRegistryService.getCustomBlockDefinitions();
	}

	@GetMapping(value = "/taskforce-blocks", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Set<Block>> getAllBlockRegistries() throws Exception {
		return blockRegistryService.getToolboxConfiguration();
	}

}
