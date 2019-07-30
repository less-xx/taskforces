package org.teapotech.block.executor;

import org.teapotech.block.model.Block;

public class BlockExecutionProgress {

	public enum BlockStatus {
		Waiting, Created, Initializing, Running, Stopped
	}

	private final String threadName;

	public BlockExecutionProgress(String threadName) {
		this.threadName = threadName;
	}

	private BlockStatus blockStatus = BlockStatus.Waiting;
	private String blockId;
	private String blockType;
	private String message;

	public BlockStatus getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(BlockStatus blockStatus) {
		this.blockStatus = blockStatus;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public void setBlock(Block block) {
		this.blockId = block.getId();
		this.blockType = block.getType();
	}

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getThreadName() {
		return threadName;
	}
}
