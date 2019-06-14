package org.teapotech.block.event;

public class FileEvent extends BlockEvent {

	public FileEvent() {
		super();
	}

	public FileEvent(String workspaceId, String blockType, String blockId) {
		super(workspaceId, blockType, blockId);
	}

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
