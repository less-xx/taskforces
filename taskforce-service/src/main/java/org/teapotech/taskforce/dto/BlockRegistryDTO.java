/**
 * 
 */
package org.teapotech.taskforce.dto;

import org.teapotech.block.BlockExecutorFactory.BlockRegistry;

/**
 * @author jiangl
 *
 */
public class BlockRegistryDTO {

	private String type;
	private String category;
	private Object def;

	public BlockRegistryDTO() {
	}

	public BlockRegistryDTO(BlockRegistry blockRegistry) {
		this.type = blockRegistry.getType();
		this.category = blockRegistry.getCategory();
	}

	public BlockRegistryDTO(BlockRegistry blockRegistry, Object definition) {
		this.type = blockRegistry.getType();
		this.category = blockRegistry.getCategory();
		this.def = definition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Object getDef() {
		return def;
	}

	public void setDef(Object def) {
		this.def = def;
	}
}
