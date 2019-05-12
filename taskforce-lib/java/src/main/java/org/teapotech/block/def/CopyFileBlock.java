package org.teapotech.block.def;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.taskforce.entity.CustomStorageConfigEntity;
import org.teapotech.taskforce.service.CustomStorageConfigService;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CopyFileBlock extends CustomBlock {

	private final static Logger LOG = LoggerFactory.getLogger(CopyFileBlock.class);

	private CustomStorageConfigService custStorageConfigService;

	@Override
	public String getBlockDefinition() {
		try {
			ObjectNode tplNode = (ObjectNode) loadBlockDefinitionTemplate();
			List<CustomStorageConfigEntity> csConf = custStorageConfigService.getAllCustomStorageConfigs();
			ArrayNode optionsNode = (ArrayNode) tplNode.get("args0").get(1).get("options");
			for (CustomStorageConfigEntity csc : csConf) {
				ArrayNode opNode = mapper.createArrayNode();
				opNode.add(csc.getName()).add(csc.getId());
				optionsNode.add(opNode);
			}
			return mapper.writeValueAsString(tplNode);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public void setCustomStorageConfigService(CustomStorageConfigService custStorageConfigService) {
		this.custStorageConfigService = custStorageConfigService;
	}

	public CustomStorageConfigService getCustomStorageConfigService() {
		return this.custStorageConfigService;
	}

	@Override
	public String getBlockType() {
		return "copy_files";
	}

}
