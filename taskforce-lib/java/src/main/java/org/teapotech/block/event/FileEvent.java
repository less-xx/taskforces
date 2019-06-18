package org.teapotech.block.event;

public class FileEvent extends BlockEvent {

	public static enum Operation {
		Create, Update, Delete
	}

	private String filePath;
	private Operation operation;

	public FileEvent() {
		super();
	}

	public FileEvent(String workspaceId, String blockType, String blockId) {
		super(workspaceId, blockType, blockId);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}
}
