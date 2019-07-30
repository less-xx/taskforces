package org.teapotech.block.exception;

import org.teapotech.block.model.Block;

public class InvalidBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5233859602493107356L;
	private final String blockId;
	private final String blockType;

	public InvalidBlockException(Block block) {
		this.blockId = block.getId();
		this.blockType = block.getType();
	}

	public InvalidBlockException(String blockId, String blockType, String message) {
		super(message);
		this.blockId = blockId;
		this.blockType = blockType;
	}

	public InvalidBlockException(String blockId, String blockType, Throwable cause) {
		super(cause);
		this.blockId = blockId;
		this.blockType = blockType;
	}

	public InvalidBlockException(String blockId, String blockType, String message, Throwable cause) {
		super(message, cause);
		this.blockId = blockId;
		this.blockType = blockType;
	}

	public String getBlockId() {
		return blockId;
	}

	public String getBlockType() {
		return blockType;
	}
}
