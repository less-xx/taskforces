/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.base.controller.LogonUserController;
import org.teapotech.block.model.Workspace;
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

	@GetMapping(value = "/custom-block-definitions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<BlockDefinitionDTO> getAllBlockDefinitions() throws Exception {
		return blockRegistryService.getCustomBlockDefinitions();
	}

	@GetMapping(value = "/taskforce-blocks", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public Workspace getAllBlockRegistries() throws Exception {
		return blockRegistryService.getToolboxConfiguration();
	}

}
